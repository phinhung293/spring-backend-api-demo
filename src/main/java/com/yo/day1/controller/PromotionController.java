package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.dto.payment.PromotionResponse;
import com.yo.day1.dto.payment.PromotionUpsertRequest;
import com.yo.day1.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<Page<PromotionResponse>> findAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ApiResponse.success(promotionService.findAll(search, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<PromotionResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(promotionService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody PromotionUpsertRequest request) {
        return ApiResponse.success(promotionService.create(request),"Promotion created");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<PromotionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PromotionUpsertRequest request) {
        return ApiResponse.success(promotionService.update(id, request),"Promotion updated");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        promotionService.delete(id);
        return ApiResponse.success( null,"Promotion deleted");
    }
}

