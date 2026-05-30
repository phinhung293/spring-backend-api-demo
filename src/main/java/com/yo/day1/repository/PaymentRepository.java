package com.yo.day1.repository;

import com.yo.day1.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceId(Long invoiceId);

    @Query("SELECT p FROM Payment p WHERE p.invoice.student.id = :studentId")
    List<Payment> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COALESCE(SUM(p.paidAmount), 0) FROM Payment p WHERE YEAR(p.paidAt) = :year AND MONTH(p.paidAt) = :month")
    java.math.BigDecimal sumPaidAmountByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT new com.yo.day1.dto.report.MonthlyRevenueDto(MONTH(p.paidAt), SUM(p.paidAmount)) " +
            "FROM Payment p WHERE YEAR(p.paidAt) = :year GROUP BY MONTH(p.paidAt) ORDER BY MONTH(p.paidAt)")
    List<com.yo.day1.dto.report.MonthlyRevenueDto> getRevenueByYear(@Param("year") int year);

    @Query("SELECT new com.yo.day1.dto.report.CourseRevenueDto(p.invoice.courseClass.course.name, SUM(p.paidAmount)) " +
            "FROM Payment p WHERE YEAR(p.paidAt) = :year GROUP BY p.invoice.courseClass.course.name")
    List<com.yo.day1.dto.report.CourseRevenueDto> getRevenueByCourse(@Param("year") int year);
}

