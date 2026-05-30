package com.yo.day1.dto.payment;


import com.yo.day1.domain.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PromotionUpsertRequest(
    @NotBlank String promoCode,
    @NotBlank String name,
    @NotNull DiscountType discountType,
    @NotNull BigDecimal discountValue,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    Boolean isActive,
    String note
) {}
