package com.yo.day1.repository;

import com.yo.day1.domain.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    Page<Promotion> findByNameContainingIgnoreCaseOrPromoCodeContainingIgnoreCase(String name, String promoCode, Pageable pageable);


}
