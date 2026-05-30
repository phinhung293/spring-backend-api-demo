package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.common.exception.BadRequestException;
import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.dto.billing.InvoiceCreateRequest;
import com.yo.day1.dto.billing.InvoiceResponse;
import com.yo.day1.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@Tag(name = "Billing", description = "Invoice and tuition billing endpoints.")
@SecurityRequirement(name = "bearerAuth")
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF', 'CASHIER')")
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoices")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF','CASHIER')")
    @Operation(summary = "Create invoice", description = "Creates a tuition invoice for a student and course class, optionally applying a promotion.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Invoice created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permission")
    })
    public ApiResponse<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceCreateRequest request) throws NotFoundException {
        return ApiResponse.success(billingService.createInvoice(request),"Invoice created");
    }

    @GetMapping("/students/{studentId}/invoices")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF','CASHIER','PARENT')")
    @Operation(summary = "List invoices by student", description = "Returns invoices for the specified student. Parents only see invoices they are allowed to access.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Invoices returned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permission"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ApiResponse<List<InvoiceResponse>> findInvoicesByStudent(@Parameter(description = "Student identifier", example = "1") @PathVariable Long studentId, @Parameter(hidden = true) Principal principal) throws BadRequestException, NotFoundException {
        return ApiResponse.success(billingService.findInvoicesByStudent(studentId, principal.getName()));
    }
    // POST http://localhost:8080/api/invoices/bulk-create
    @PostMapping("/bulk-create")
    public ResponseEntity<List<InvoiceResponse>> createInvoices(@Valid @RequestBody InvoiceCreateRequest req) {
        return ResponseEntity.ok(billingService.createInvoicesForTwoMonths(req));
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<InvoiceResponse>> getStudentInvoices(
            @PathVariable Long studentId,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {

        return ResponseEntity.ok(billingService.getInvoicesByStudent(studentId, month, year));
    }
    // POST http://localhost:8080/api/billing/bulk
    @PostMapping("/bulk")
    public ResponseEntity<List<InvoiceResponse>> createInvoicesForTwoMonths(@Valid @RequestBody InvoiceCreateRequest request) {
        return ResponseEntity.ok(billingService.createInvoicesForTwoMonths(request));
    }
    // GET http://localhost:8080/api/billing/student/1?month=&year=
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<InvoiceResponse>> getStudentInvoicesWithFilter(
            @PathVariable Long studentId,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            java.security.Principal principal) {
        return ResponseEntity.ok(billingService.findInvoicesByStudentAndFilter(studentId, month, year, principal.getName()));
    }
}
