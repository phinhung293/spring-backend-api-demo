package com.yo.day1.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CourseUpsertRequest(
        @NotBlank(message = "Mã khóa học không được để trống")
        String courseCode,

        @NotBlank(message = "Tên khóa học không được để trống")
        String name,

        @NotNull(message = "Học phí không được để trống")
        BigDecimal tuitionFee,

        String description
) {
}
