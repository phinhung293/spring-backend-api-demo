package com.yo.day1.repository;

import com.yo.day1.domain.entity.LearningResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LearningResultRepository extends JpaRepository<LearningResult, Long> {
    boolean existsByStudentIdAndCourseClassIdAndResultMonth(Long studentId, Long courseClassId,
                                                            java.time.LocalDate resultMonth);

    java.util.List<LearningResult> findByStudentId(Long studentId);
    // Truy vấn lọc dữ liệu điểm số theo tháng và năm của lớp học
    @Query("SELECT lr FROM LearningResult lr WHERE lr.courseClass.id = :classId " +
            "AND (:month IS NULL OR FUNCTION('MONTH', lr.resultMonth) = :month) " +
            "AND (:year IS NULL OR FUNCTION('YEAR', lr.resultMonth) = :year)")
    List<LearningResult> findByClassIdAndMonthAndYear(
            @Param("classId") Long classId,
            @Param("month") Integer month,
            @Param("year") Integer year);

}
