package com.yo.day1.service;

import com.yo.day1.domain.entity.ScheduleSlot;
import com.yo.day1.dto.schedule.ScheduleSlotResponse;
import com.yo.day1.dto.schedule.ScheduleSlotUpsertRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ScheduleSlotService {
    ScheduleSlotResponse findById(Long id);
    Page<ScheduleSlotResponse> findAll(String search, Pageable pageable);
    public ScheduleSlot save(ScheduleSlot scheduleSlot);
    void delete(Long id);
    ScheduleSlotResponse create(ScheduleSlotUpsertRequest request);
    ScheduleSlotResponse update(Long id, ScheduleSlotUpsertRequest request);

}
