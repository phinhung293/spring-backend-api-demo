package com.yo.day1.dto.report;

import java.math.BigDecimal;

public record DashboardStatsResponse(
    long studentsCount,
    long coursesCount,
    long classesCount,
    BigDecimal currentMonthRevenue,
    long unpaidInvoicesCount
) {}
