package com.yo.day1.dto.enrollment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseClassId,
        String classCode,
        String className,
        String courseName,
        String mainTeacherName,
        java.math.BigDecimal tuitionFee,
        java.time.LocalDate enrolledAt,
        String status,
        String note,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt
) {
}
