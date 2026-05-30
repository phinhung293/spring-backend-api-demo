package com.yo.day1.service.impl;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.ScheduleSlot;
import com.yo.day1.dto.schedule.ScheduleSlotResponse;
import com.yo.day1.dto.schedule.ScheduleSlotUpsertRequest;
import com.yo.day1.repository.ScheduleSlotRepository;
import com.yo.day1.service.ScheduleSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleSlotServiceImpl implements ScheduleSlotService {
    private final ScheduleSlotRepository scheduleSlotRepository;

    public ScheduleSlot save(ScheduleSlot scheduleSlot) {
        return scheduleSlotRepository.save(scheduleSlot);
    }
    public void deleteById(Long id) {
        scheduleSlotRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true) // Thêm tối ưu hóa transaction chỉ đọc
    public Page<ScheduleSlotResponse> findAll(String search, Pageable pageable) {
        Page<ScheduleSlot> page;
        if (search != null && !search.isBlank()) {
            page = scheduleSlotRepository.findBySlotCodeContainingIgnoreCase(search, pageable);
        } else {
            page = scheduleSlotRepository.findAll(pageable);
        }
        return page.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true) // Thêm tối ưu hóa transaction chỉ đọc
    public ScheduleSlotResponse findById(Long id) {
        return scheduleSlotRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("ScheduleSlot not found with id: " + id));
    }

    @Override
    @Transactional
    public ScheduleSlotResponse create(ScheduleSlotUpsertRequest request) {
        ScheduleSlot slot = new ScheduleSlot();
        updateEntity(slot, request);
        return mapToResponse(scheduleSlotRepository.save(slot));
    }

    @Override
    @Transactional
    public ScheduleSlotResponse update(Long id, ScheduleSlotUpsertRequest request) {
        ScheduleSlot slot = scheduleSlotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ScheduleSlot not found with id: " + id));
        updateEntity(slot, request);
        return mapToResponse(scheduleSlotRepository.save(slot));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!scheduleSlotRepository.existsById(id)) {
            throw new NotFoundException("ScheduleSlot not found with id: " + id);
        }
        scheduleSlotRepository.deleteById(id);
    }
    private void updateEntity(ScheduleSlot slot, ScheduleSlotUpsertRequest request) {
        slot.setSlotCode(request.slotCode());
        slot.setWeekday(request.weekday().byteValue());
        slot.setStartTime(request.startTime());
        slot.setEndTime(request.endTime());
        slot.setNote(request.note());
    }
    private ScheduleSlotResponse mapToResponse(ScheduleSlot slot) {
        return new ScheduleSlotResponse(
                slot.getId(),
                slot.getSlotCode(),
                Integer.valueOf(slot.getWeekday()),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getNote()
        );
    }
}
