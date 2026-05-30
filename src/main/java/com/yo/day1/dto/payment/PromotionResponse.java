package com.yo.day1.dto.payment;


import com.yo.day1.domain.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromotionResponse(
    Long id,
    String promoCode,
    String name,
    DiscountType discountType,
    BigDecimal discountValue,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isActive,
    String note
) {}
