package com.yo.day1.service.impl;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Payment;
import com.yo.day1.domain.entity.TuitionInvoice;
import com.yo.day1.domain.enums.InvoiceStatus;
import com.yo.day1.dto.payment.PaymentResponse;
import com.yo.day1.repository.PaymentRepository;
import com.yo.day1.repository.TuitionInvoiceRepository;
import com.yo.day1.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final TuitionInvoiceRepository tuitionInvoiceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findByStudentId(Long studentId) {
        return paymentRepository.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse findById(Long id) {
        return paymentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + id));
    }

    // Hàm chuyển đổi sang Record bằng Constructor
    private PaymentResponse toResponse(Payment payment) {
        TuitionInvoice invoice = payment.getInvoice();
        String invoiceCode = invoice != null ? invoice.getInvoiceCode() : null;
        String studentName = (invoice != null && invoice.getStudent() != null) ? invoice.getStudent().getFullName() : null;
        String className = (invoice != null && invoice.getCourseClass() != null) ? invoice.getCourseClass().getName() : null;
        String billingMonth = (invoice != null && invoice.getBillingMonth() != null) ? invoice.getBillingMonth().toString() : null;

        java.math.BigDecimal originalAmount = invoice != null ? invoice.getOriginalAmount() : java.math.BigDecimal.ZERO;
        java.math.BigDecimal discountAmount = invoice != null ? invoice.getDiscountAmount() : java.math.BigDecimal.ZERO;
        java.math.BigDecimal finalAmount = invoice != null ? invoice.getFinalAmount() : java.math.BigDecimal.ZERO;
        java.math.BigDecimal balanceAmount = invoice != null ? invoice.getBalanceAmount() : java.math.BigDecimal.ZERO;

        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentCode(),
                payment.getPaidAmount(),
                payment.getPaymentMethod(),
                payment.getPaidAt(),
                payment.getCashierUser() != null ? payment.getCashierUser().getFullName() : "Hệ thống",
                payment.getNote(),
                invoiceCode,
                studentName,
                className,
                billingMonth,
                originalAmount,
                discountAmount,
                finalAmount,
                balanceAmount
        );
    }
    public void updateInvoiceStatusAfterPayment(TuitionInvoice invoice, BigDecimal newPaidAmount) {
        // Cộng dồn số tiền mới đóng vào tổng số tiền đã thanh toán trước đó
        BigDecimal totalPaid = invoice.getAmountPaid().add(newPaidAmount);
        invoice.setAmountPaid(totalPaid);

        // So sánh số tiền đã đóng với số tiền phải đóng (amountDue)
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(InvoiceStatus.UNPAID);
        } else if (totalPaid.compareTo(invoice.getAmountPaid()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIAL);
        }

        invoice.setUpdatedAt(LocalDateTime.now());
        tuitionInvoiceRepository.save(invoice);
    }
}
