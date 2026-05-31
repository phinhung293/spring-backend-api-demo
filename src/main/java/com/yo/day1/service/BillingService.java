package com.yo.day1.service;

import com.yo.day1.common.exception.BadRequestException;
import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Promotion;
import com.yo.day1.domain.entity.TuitionInvoice;
import com.yo.day1.dto.billing.InvoiceCreateRequest;
import com.yo.day1.dto.billing.InvoiceResponse;
import com.yo.day1.dto.billing.PaymentCreateRequest;
import com.yo.day1.dto.billing.PaymentResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BillingService {
    InvoiceResponse createInvoice(InvoiceCreateRequest request) throws NotFoundException;
    List<InvoiceResponse> findInvoicesByStudent(Long studentId, String username) throws BadRequestException, NotFoundException;
    List<InvoiceResponse> createInvoicesForTwoMonths(InvoiceCreateRequest request) throws NotFoundException;
    List<InvoiceResponse> findInvoicesByStudentAndFilter(Long studentId, Integer month, Integer year, String username) throws BadRequestException, NotFoundException;
    List<InvoiceResponse> getInvoicesByStudent(Long studentId, Integer month, Integer year);
    PaymentResponse createPayment(PaymentCreateRequest request, String username) throws NotFoundException, BadRequestException;
}
