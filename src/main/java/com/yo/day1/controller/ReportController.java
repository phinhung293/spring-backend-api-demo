package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.dto.report.CourseRevenueDto;
import com.yo.day1.dto.report.DashboardStatsResponse;
import com.yo.day1.dto.report.MonthlyRevenueDto;
import com.yo.day1.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard-stats")
    public ApiResponse<DashboardStatsResponse> getDashboardStats() {
        return ApiResponse.success(reportService.getDashboardStats());
    }

    @GetMapping("/revenue/monthly")
    public ApiResponse<List<MonthlyRevenueDto>> getMonthlyRevenue(@RequestParam(required = false) Integer year) {
        int targetYear = year != null ? year : LocalDate.now().getYear();
        return ApiResponse.success(reportService.getRevenueByYear(targetYear));
    }

    @GetMapping("/revenue/course")
    public ApiResponse<List<CourseRevenueDto>> getCourseRevenue(@RequestParam(required = false) Integer year) {
        int targetYear = year != null ? year : LocalDate.now().getYear();
        return ApiResponse.success(reportService.getRevenueByCourse(targetYear));
    }
}

