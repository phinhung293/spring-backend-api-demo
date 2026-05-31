package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.dto.billing.PaymentResponse;
import com.yo.day1.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<List<PaymentResponse>> findByStudentId(@PathVariable Long studentId) {
        return ApiResponse.success(paymentService.findByStudentId(studentId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<List<PaymentResponse>> findAll() {
        return ApiResponse.success(paymentService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<PaymentResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(paymentService.findById(id));
    }
}

