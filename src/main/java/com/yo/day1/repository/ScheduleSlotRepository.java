package com.yo.day1.repository;

import com.yo.day1.domain.entity.ScheduleSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleSlotRepository  extends JpaRepository<ScheduleSlot, Long> {
    Page<ScheduleSlot> findBySlotCodeContainingIgnoreCase(String slotCode, Pageable pageable);

}
