package com.yo.day1.dto.billing;


import com.yo.day1.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String paymentCode,
        BigDecimal paidAmount,
        PaymentMethod paymentMethod,
        LocalDateTime paidAt,
        String cashierName,
        String note,
        String invoiceCode,
        String studentName,
        // Invoice details
        String className,
        String billingMonth,
        BigDecimal originalAmount,
        BigDecimal discountAmount,
        BigDecimal finalAmount,
        BigDecimal balanceAmount
) {
}
