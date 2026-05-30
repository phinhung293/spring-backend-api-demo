package com.yo.day1.dto.schedule;

import java.time.LocalTime;

public record ScheduleSlotResponse(
    Long id,
    String slotCode,
    Integer weekday,
    LocalTime startTime,
    LocalTime endTime,
    String note
) {}
