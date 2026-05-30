package com.yo.day1.service;

import com.yo.day1.dto.room.RoomResponse;
import com.yo.day1.dto.room.RoomUpsertRequest;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    public List<RoomResponse> findAll();
    public Optional<RoomResponse> findById(long id);
    public RoomResponse save(RoomUpsertRequest req);
    public RoomResponse save(long id,RoomUpsertRequest req);
}
