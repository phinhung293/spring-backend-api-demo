package com.yo.day1.service.impl;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Promotion;
import com.yo.day1.domain.enums.DiscountType;
import com.yo.day1.dto.payment.PromotionResponse;
import com.yo.day1.dto.payment.PromotionUpsertRequest;
import com.yo.day1.repository.PromotionRepository;
import com.yo.day1.service.PromotionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    @PostConstruct
    @Transactional
    public void migrateData() {
        List<Promotion> promotions = promotionRepository.findAll();
        boolean changed = false;
        for (Promotion p : promotions) {
            if (p.getDiscountType() == DiscountType.PERCENT) {
                p.setDiscountType(DiscountType.PERCENT);
                changed = true;
            } else if (p.getDiscountType() == DiscountType.AMOUNT) {
                p.setDiscountType(DiscountType.AMOUNT);
                changed = true;
            }
        }
        if (changed) {
            promotionRepository.saveAll(promotions);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionResponse> findAll(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return promotionRepository
                    .findByNameContainingIgnoreCaseOrPromoCodeContainingIgnoreCase(search, search, pageable)
                    .map(this::toResponse);
        }
        return promotionRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResponse findById(Long id) {
        return promotionRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + id));
    }

    @Override
    @Transactional
    public PromotionResponse create(PromotionUpsertRequest request) {
        Promotion promotion = new Promotion();
        apply(promotion, request);
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    @Transactional
    public PromotionResponse update(Long id, PromotionUpsertRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + id));
        apply(promotion, request);
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new NotFoundException("Promotion not found with id: " + id);
        }
        promotionRepository.deleteById(id);
    }

    private void apply(Promotion promotion, PromotionUpsertRequest request) {
        promotion.setPromoCode(request.promoCode());
        promotion.setName(request.name());
        promotion.setDiscountType(request.discountType());
        promotion.setDiscountValue(request.discountValue());
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        if (request.isActive() != null) {
            promotion.setIsActive(request.isActive());
        }
        promotion.setNote(request.note());
    }

    private PromotionResponse toResponse(Promotion p) {
        BigDecimal discountValue = p.getDiscountValue() instanceof BigDecimal
                ? (BigDecimal) p.getDiscountValue()
                : BigDecimal.valueOf(((Number) p.getDiscountValue()).doubleValue());

        return new PromotionResponse(
                p.getId(),
                p.getPromoCode(),
                p.getName(),
                p.getDiscountType(),
                discountValue,
                p.getStartDate(),
                p.getEndDate(),
                p.getIsActive(),
                p.getNote()
        );
    }
}
