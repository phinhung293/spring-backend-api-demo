package com.yo.day1.service.impl;

import com.yo.day1.domain.enums.InvoiceStatus;
import com.yo.day1.dto.report.CourseRevenueDto;
import com.yo.day1.dto.report.DashboardStatsResponse;
import com.yo.day1.dto.report.MonthlyRevenueDto;
import com.yo.day1.repository.*;
import com.yo.day1.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseClassRepository courseClassRepository;
    private final TuitionInvoiceRepository tuitionInvoiceRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        long studentsCount = studentRepository.count();
        long coursesCount = courseRepository.count();
        long classesCount = courseClassRepository.count();
        long unpaidInvoicesCount = tuitionInvoiceRepository.countByStatusNot(InvoiceStatus.PAID);

        LocalDate now = LocalDate.now();
        BigDecimal currentMonthRevenue = paymentRepository.sumPaidAmountByMonth(now.getYear(), now.getMonthValue());

        // Xử lý chống lỗi NullPointerException nếu tháng hiện tại chưa có phát sinh giao dịch nào
        if (currentMonthRevenue == null) {
            currentMonthRevenue = BigDecimal.ZERO;
        }

        // Khởi tạo và trả về thông qua constructor của Record
        return new DashboardStatsResponse(
                studentsCount,
                coursesCount,
                classesCount,
                currentMonthRevenue,
                unpaidInvoicesCount
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyRevenueDto> getRevenueByYear(int year) {
        return paymentRepository.getRevenueByYear(year);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseRevenueDto> getRevenueByCourse(int year) {
        return paymentRepository.getRevenueByCourse(year);
    }
}
