package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.domain.entity.Promotion;
import com.yo.day1.domain.entity.Room;
import com.yo.day1.domain.entity.ScheduleSlot;
import com.yo.day1.domain.entity.Teacher;
import com.yo.day1.repository.PromotionRepository;
import com.yo.day1.repository.RoomRepository;
import com.yo.day1.repository.ScheduleSlotRepository;
import com.yo.day1.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final TeacherRepository teacherRepository;
    private final RoomRepository roomRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final PromotionRepository promotionRepository;
//B1: Viết thêm 1 endpoint test trả về timestamp hiện tại
    @GetMapping("/current-timestamp")
    public ApiResponse<LocalDateTime> getCurrentTimestamp() {
        return ApiResponse.success(LocalDateTime.now());
    }
    @GetMapping("/teachers")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<List<Teacher>> teachers() {
        return ApiResponse.success(teacherRepository.findAll());
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<List<Room>> rooms() {
        return ApiResponse.success(roomRepository.findAll());
    }

    @GetMapping("/schedule-slots")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF')")
    public ApiResponse<List<ScheduleSlot>> scheduleSlots() {
        return ApiResponse.success(scheduleSlotRepository.findAll());
    }

    @GetMapping("/promotions")
    @PreAuthorize("hasAnyRole('ADMIN','ACADEMIC_STAFF','CASHIER')")
    public ApiResponse<List<Promotion>> promotions() {
        return ApiResponse.success(promotionRepository.findAll());
    }
}

