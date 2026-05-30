package com.yo.day1.service.impl;

import com.yo.day1.common.exception.BadRequestException;
import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Promotion;
import com.yo.day1.domain.entity.TuitionInvoice;
import com.yo.day1.domain.entity.User;
import com.yo.day1.domain.enums.DiscountType;
import com.yo.day1.domain.enums.InvoiceStatus;
import com.yo.day1.dto.billing.InvoiceCreateRequest;
import com.yo.day1.dto.billing.InvoiceResponse;
import com.yo.day1.repository.PromotionRepository;
import com.yo.day1.repository.TuitionInvoiceRepository;
import com.yo.day1.service.AuthService;
import com.yo.day1.service.BillingService;
import com.yo.day1.service.CourseClassService;
import com.yo.day1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final TuitionInvoiceRepository tuitionInvoiceRepository;
    private final PromotionRepository promotionRepository;
    private final StudentService studentService;
    private final CourseClassService courseClassService;
    private final AuthService authService;
    private final ModelMapper mapper;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceCreateRequest request) throws NotFoundException {
        TuitionInvoice invoice = new TuitionInvoice();
        invoice.setInvoiceCode(request.getInvoiceCode());
        invoice.setStudent(studentService.getStudent(request.getStudentId()));
        invoice.setCourseClass(courseClassService.getCourseClass(request.getCourseClassId()));
        invoice.setBillingMonth(request.getBillingMonth());

        BigDecimal originalAmount = request.getOriginalAmount() != null
                ? request.getOriginalAmount()
                : invoice.getCourseClass().getTuitionFee();
        invoice.setOriginalAmount(originalAmount);

        Promotion promotion = null;
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getPromotionId() != null) {
            promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new NotFoundException("Promotion not found: " + request.getPromotionId()));
            discountAmount = calculateDiscount(originalAmount, promotion);
        }

        // Thực hiện các phép toán subtract và so sánh với giá trị tối thiểu là 0
        BigDecimal finalAmount = originalAmount.subtract(discountAmount).max(BigDecimal.ZERO);
        invoice.setPromotion(promotion);
        invoice.setDiscountAmount(discountAmount);
        invoice.setFinalAmount(finalAmount);
        invoice.setAmountPaid(BigDecimal.ZERO);
        invoice.setBalanceAmount(finalAmount);

        // Kiểm tra trạng thái PAID nếu số tiền cuối cùng bằng 0
        invoice.setStatus(finalAmount.compareTo(BigDecimal.ZERO) == 0 ? InvoiceStatus.PAID : InvoiceStatus.UNPAID);
        invoice.setDueDate(request.getDueDate());
        invoice.setNote(request.getNote());

        return toInvoiceResponse(tuitionInvoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> findInvoicesByStudent(Long studentId, String username) throws BadRequestException, NotFoundException {
        User user = authService.findActiveUserByUsername(username);
        if (user.getRole().name().equals("PARENT")) {
            studentService.getStudentForParent(studentId, user.getParent().getId());
        }
        return tuitionInvoiceRepository.findByStudentId(studentId).stream().map(this::toInvoiceResponse).toList();
    }

    // Hàm tính toán giảm giá sử dụng các phép nhân chia an toàn của BigDecimal
    private BigDecimal calculateDiscount(BigDecimal originalAmount, Promotion promotion) {
        if (promotion.getDiscountType() == DiscountType.PERCENT) {
            // Chuyển đổi phần trăm giảm giá sang BigDecimal rồi nhân chia
            BigDecimal discountValue = promotion.getDiscountValue();
            return originalAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
        }
        return promotion.getDiscountValue();
    }

    private InvoiceResponse toInvoiceResponse(TuitionInvoice item) {
        InvoiceResponse result = mapper.map(item, InvoiceResponse.class);
        result.setStudentId(item.getStudent().getId());
        result.setStudentName(item.getStudent().getFullName());
        result.setCourseClassId(item.getCourseClass().getId());
        result.setClassName(item.getCourseClass().getName());
        result.setStatus(item.getStatus().name());
        if (item.getPromotion() != null) {
            result.setPromotionId(item.getPromotion().getId());
            result.setPromotionName(item.getPromotion().getName());
        }
        return result;
    }
    @Transactional
    public List<InvoiceResponse> createInvoicesForTwoMonths(InvoiceCreateRequest request) throws NotFoundException {
        List<TuitionInvoice> invoicesToSave = new ArrayList<>();

        // Xác định tháng thứ 1 và tháng thứ 2 liên tiếp
        LocalDate firstMonth = request.getBillingMonth().withDayOfMonth(1);
        LocalDate secondMonth = firstMonth.plusMonths(1);

        // Khởi tạo hóa đơn tháng thứ 1 (nếu chưa tồn tại)
        if (!tuitionInvoiceRepository.existsByStudentIdAndCourseClassIdAndBillingMonth(request.getStudentId(), request.getCourseClassId(), firstMonth)) {
            TuitionInvoice inv1 = new TuitionInvoice();
            inv1.setInvoiceCode(request.getInvoiceCode() + "-M1");
            inv1.setStudent(studentService.getStudent(request.getStudentId()));
            inv1.setCourseClass(courseClassService.getCourseClass(request.getCourseClassId()));
            inv1.setBillingMonth(firstMonth);

            BigDecimal originalAmount = request.getOriginalAmount() != null ? request.getOriginalAmount() : inv1.getCourseClass().getTuitionFee();
            inv1.setOriginalAmount(originalAmount);

            Promotion promotion = null;
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (request.getPromotionId() != null) {
                promotion = promotionRepository.findById(request.getPromotionId()).orElse(null);
                if (promotion != null) discountAmount = calculateDiscount(originalAmount, promotion);
            }

            BigDecimal finalAmount = originalAmount.subtract(discountAmount).max(BigDecimal.ZERO);
            inv1.setPromotion(promotion);
            inv1.setDiscountAmount(discountAmount);
            inv1.setFinalAmount(finalAmount);
            inv1.setAmountPaid(BigDecimal.ZERO);
            inv1.setBalanceAmount(finalAmount);
            inv1.setStatus(finalAmount.compareTo(BigDecimal.ZERO) == 0 ? InvoiceStatus.PAID : InvoiceStatus.UNPAID);
            inv1.setDueDate(request.getDueDate());
            inv1.setNote(request.getNote());
            invoicesToSave.add(inv1);
        }

        // Khởi tạo hóa đơn tháng thứ 2 kế tiếp (nếu chưa tồn tại)
        if (!tuitionInvoiceRepository.existsByStudentIdAndCourseClassIdAndBillingMonth(request.getStudentId(), request.getCourseClassId(), secondMonth)) {
            TuitionInvoice inv2 = new TuitionInvoice();
            inv2.setInvoiceCode(request.getInvoiceCode() + "-M2");
            inv2.setStudent(studentService.getStudent(request.getStudentId()));
            inv2.setCourseClass(courseClassService.getCourseClass(request.getCourseClassId()));
            inv2.setBillingMonth(secondMonth);

            BigDecimal originalAmount = inv2.getCourseClass().getTuitionFee(); // Tháng sau tính theo giá gốc của lớp học
            inv2.setOriginalAmount(originalAmount);
            inv2.setDiscountAmount(BigDecimal.ZERO);
            inv2.setFinalAmount(originalAmount);
            inv2.setAmountPaid(BigDecimal.ZERO);
            inv2.setBalanceAmount(originalAmount);
            inv2.setStatus(originalAmount.compareTo(BigDecimal.ZERO) == 0 ? InvoiceStatus.PAID : InvoiceStatus.UNPAID);
            inv2.setDueDate(request.getDueDate() != null ? request.getDueDate().plusMonths(1) : null);
            inv2.setNote(request.getNote());
            invoicesToSave.add(inv2);
        }

        if (invoicesToSave.isEmpty()) {
            throw new BadRequestException("Hóa đơn của cả 2 tháng này đều đã được khởi tạo trước đó!");
        }

        return tuitionInvoiceRepository.saveAll(invoicesToSave).stream()
                .map(this::toInvoiceResponse)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<InvoiceResponse> findInvoicesByStudentAndFilter(Long studentId, Integer month, Integer year, String username) throws BadRequestException, NotFoundException {
        User user = authService.findActiveUserByUsername(username);
        if (user.getRole().name().equals("PARENT")) {
            studentService.getStudentForParent(studentId, user.getParent().getId());
        }
        return tuitionInvoiceRepository.findByStudentIdAndFilter(studentId, month, year).stream()
                .map(this::toInvoiceResponse)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByStudent(Long studentId, Integer month, Integer year) {
        return tuitionInvoiceRepository.findByStudentIdAndFilter(studentId, month, year).stream()
                .map(invoice -> mapper.map(invoice, InvoiceResponse.class))
                .toList();
    }
}
