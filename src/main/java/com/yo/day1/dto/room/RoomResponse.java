package com.yo.day1.dto.room;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class RoomResponse {
    private long id;
    private String roomCode;

    private String name;

    private int capacity;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
