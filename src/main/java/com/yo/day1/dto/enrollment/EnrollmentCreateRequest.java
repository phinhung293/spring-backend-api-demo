package com.yo.day1.dto.enrollment;


import com.yo.day1.domain.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EnrollmentCreateRequest(
        @NotNull Long studentId,
        @NotNull Long courseClassId,
        @NotNull LocalDate enrolledAt,
        @NotNull EnrollmentStatus status,
        @Size(max = 255) String note
) {
}
