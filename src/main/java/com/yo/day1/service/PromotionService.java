package com.yo.day1.service;

import com.yo.day1.dto.payment.PromotionResponse;
import com.yo.day1.dto.payment.PromotionUpsertRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    Page<PromotionResponse> findAll(String search, Pageable pageable);
    PromotionResponse findById(Long id);
    PromotionResponse create(PromotionUpsertRequest request);
    PromotionResponse update(Long id, PromotionUpsertRequest request);
    void delete(Long id);
}
