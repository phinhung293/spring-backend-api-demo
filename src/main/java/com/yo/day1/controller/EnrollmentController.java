package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.dto.enrollment.EnrollmentCreateRequest;
import com.yo.day1.dto.enrollment.EnrollmentResponse;
import com.yo.day1.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<EnrollmentResponse> create(@Valid @RequestBody EnrollmentCreateRequest request) {
        return ApiResponse.success(enrollmentService.create(request),"Enrollment created");
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<List<EnrollmentResponse>> findByClassId(@PathVariable Long classId) {
        return ApiResponse.success(enrollmentService.findByClassId(classId));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF','PARENT')")
    public ApiResponse<List<EnrollmentResponse>> findByStudentId(@PathVariable Long studentId, java.security.Principal principal) {
        return ApiResponse.success(enrollmentService.findByStudentId(studentId, principal.getName()));
    }
}

