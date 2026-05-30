package com.yo.day1.dto.report;

import java.math.BigDecimal;

public record CourseRevenueDto(
    String courseName,
    BigDecimal totalRevenue
) {}
