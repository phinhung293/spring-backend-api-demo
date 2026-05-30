package com.yo.day1.repository;

import com.yo.day1.domain.entity.TuitionInvoice;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TuitionInvoiceRepository extends JpaRepository<TuitionInvoice,Long> {
    java.util.List<TuitionInvoice> findByStudentId(Long studentId);

    java.util.List<TuitionInvoice> findByStudentParentId(Long parentId);
    long countByStatusNot(com.yo.day1.domain.enums.InvoiceStatus status);
    @Query("SELECT ti FROM TuitionInvoice ti WHERE ti.student.id = :studentId " +
            "AND (:month IS NULL OR FUNCTION('MONTH', ti.billingMonth) = :month) " +
            "AND (:year IS NULL OR FUNCTION('YEAR', ti.billingMonth) = :year)")
    List<TuitionInvoice> findByStudentIdAndFilter(
            @Param("studentId") Long studentId,
            @Param("month") Integer month,
            @Param("year") Integer year);

    boolean existsByStudentIdAndCourseClassIdAndBillingMonth(Long studentId, Long courseClassId, LocalDate billingMonth);
}
