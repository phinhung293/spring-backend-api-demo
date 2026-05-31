package com.yo.day1.service.impl;

import com.yo.day1.common.exception.BadRequestException;
import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Payment;
import com.yo.day1.domain.entity.Promotion;
import com.yo.day1.domain.entity.TuitionInvoice;
import com.yo.day1.domain.entity.User;
import com.yo.day1.domain.enums.DiscountType;
import com.yo.day1.domain.enums.InvoiceStatus;
import com.yo.day1.dto.billing.InvoiceCreateRequest;
import com.yo.day1.dto.billing.InvoiceResponse;
import com.yo.day1.dto.billing.PaymentCreateRequest;
import com.yo.day1.dto.billing.PaymentResponse;
import com.yo.day1.repository.EnrollmentRepository;
import com.yo.day1.repository.PaymentRepository;
import com.yo.day1.repository.PromotionRepository;
import com.yo.day1.repository.TuitionInvoiceRepository;
import com.yo.day1.service.*;
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
    private final PaymentRepository paymentRepository;
    private final EnrollmentService enrollmentService;

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
    @Transactional
    public PaymentResponse createPayment(PaymentCreateRequest request, String username) throws NotFoundException, BadRequestException {
        // 1. Kiểm tra hóa đơn tồn tại không
        TuitionInvoice invoice = tuitionInvoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new NotFoundException("Invoice not found: " + request.getInvoiceId()));

        // 2. Validate số tiền đóng vào phải lớn hơn 0 (Dùng compareTo với BigDecimal.ZERO)
        if (request.getPaidAmount() == null || request.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Paid amount must be greater than 0");
        }

        // 3. Khởi tạo và lưu thông tin giao dịch Payment
        User cashier = authService.findActiveUserByUsername(username);
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setPaymentCode(request.getPaymentCode());
        payment.setPaidAmount(request.getPaidAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaidAt(request.getPaidAt());
        payment.setCashierUser(cashier);
        payment.setNote(request.getNote());
        Payment savedPayment = paymentRepository.save(payment);

        // 4. CHUẨN HÓA BIGDECIMAL: Tính toán lại tiền đã đóng và tiền dư (balance) trên Invoice
        // Tổng tiền đã đóng mới = Tiền đã đóng cũ + Tiền vừa đóng thêm
        BigDecimal newAmountPaid = invoice.getAmountPaid().add(request.getPaidAmount());

        // Số tiền còn lại phải đóng (balance) = Số tiền cuối cùng của hóa đơn - Tổng tiền đã đóng mới
        BigDecimal balance = invoice.getFinalAmount().subtract(newAmountPaid);

        invoice.setAmountPaid(newAmountPaid);
        invoice.setBalanceAmount(balance);

        // Tính toán trạng thái mới dựa trên số tiền còn lại và số tiền đã đóng
        invoice.setStatus(calculateInvoiceStatus(balance, newAmountPaid));
        tuitionInvoiceRepository.save(invoice);

        return toPaymentResponse(savedPayment);
    }

    // Hàm bổ trợ tính trạng thái hóa đơn chuẩn cú pháp so sánh BigDecimal
    private InvoiceStatus calculateInvoiceStatus(BigDecimal balance, BigDecimal amountPaid) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return InvoiceStatus.OVERPAID; // Đóng dư tiền
        }
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            return InvoiceStatus.PAID; // Đã hoàn thành học phí
        }
        if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            return InvoiceStatus.PARTIAL; // Mới đóng được một phần
        }
        return InvoiceStatus.UNPAID; // Chưa đóng đồng nào
    }
    // Hàm map dữ liệu sang PaymentResponse
    private PaymentResponse toPaymentResponse(Payment item) {
        return new PaymentResponse(
                item.getId(),                                                                     // 1. Long id
                item.getPaymentCode(),                                                            // 2. String paymentCode
                item.getPaidAmount(),                                                             // 3. BigDecimal paidAmount
                item.getPaymentMethod(),                                                          // 4. PaymentMethod paymentMethod (Truyền thẳng Enum, không .toString())
                item.getPaidAt(),                                                                 // 5. LocalDateTime paidAt
                item.getCashierUser() != null ? item.getCashierUser().getFullName() : null,       // 6. String cashierName
                item.getNote(),                                                                   // 7. String note
                item.getInvoice() != null ? item.getInvoice().getInvoiceCode() : null,            // 8. String invoiceCode
                item.getInvoice() != null && item.getInvoice().getStudent() != null ? item.getInvoice().getStudent().getFullName() : null, // 9. String studentName

                // Các thông tin bổ sung lấy gián tiếp từ Invoice sang (Invoice details)
                item.getInvoice() != null && item.getInvoice().getCourseClass() != null ? item.getInvoice().getCourseClass().getName() : null, // 10. String className
                item.getInvoice() != null && item.getInvoice().getBillingMonth() != null ? item.getInvoice().getBillingMonth().toString() : null, // 11. String billingMonth
                item.getInvoice() != null ? item.getInvoice().getOriginalAmount() : null,         // 12. BigDecimal originalAmount
                item.getInvoice() != null ? item.getInvoice().getDiscountAmount() : null,         // 13. BigDecimal discountAmount
                item.getInvoice() != null ? item.getInvoice().getFinalAmount() : null,            // 14. BigDecimal finalAmount
                item.getInvoice() != null ? item.getInvoice().getBalanceAmount() : null           // 15. BigDecimal balanceAmount
        );
    }
}
