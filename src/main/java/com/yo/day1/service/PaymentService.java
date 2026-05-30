package com.yo.day1.service;

import com.yo.day1.dto.payment.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse findById(Long id);
    List<PaymentResponse> findByStudentId(Long studentId);
    List<PaymentResponse> findAll();
}
