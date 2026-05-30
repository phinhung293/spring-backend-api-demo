package com.yo.day1.service;

import com.yo.day1.dto.report.CourseRevenueDto;
import com.yo.day1.dto.report.DashboardStatsResponse;
import com.yo.day1.dto.report.MonthlyRevenueDto;

import java.util.List;

public interface ReportService {
    DashboardStatsResponse getDashboardStats();
    List<MonthlyRevenueDto> getRevenueByYear(int year);
    List<CourseRevenueDto> getRevenueByCourse(int year);
}
