package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.dto.schedule.ScheduleSlotResponse;
import com.yo.day1.dto.schedule.ScheduleSlotUpsertRequest;
import com.yo.day1.service.ScheduleSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule-slots")
@RequiredArgsConstructor
public class ScheduleSlotController {

    private final ScheduleSlotService scheduleSlotService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<Page<ScheduleSlotResponse>> findAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ApiResponse.success(scheduleSlotService.findAll(search, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<ScheduleSlotResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(scheduleSlotService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<ScheduleSlotResponse> create(@Valid @RequestBody ScheduleSlotUpsertRequest request) {
        return ApiResponse.success(scheduleSlotService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<ScheduleSlotResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleSlotUpsertRequest request) {
        return ApiResponse.success(scheduleSlotService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        scheduleSlotService.delete(id);
        return ApiResponse.success(null);
    }
}

