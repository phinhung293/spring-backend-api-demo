package com.yo.day1.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ScheduleSlotUpsertRequest(
    @NotBlank String slotCode,
    @NotNull Integer weekday,
    @NotNull LocalTime startTime,
    @NotNull LocalTime endTime,
    String note
) {}
