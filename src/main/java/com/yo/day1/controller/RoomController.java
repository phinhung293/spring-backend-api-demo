package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.dto.room.RoomResponse;
import com.yo.day1.dto.room.RoomUpsertRequest;
import com.yo.day1.service.RoomService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/rooms")
public class RoomController {
    private final RoomService roomService;
    @GetMapping
    public ApiResponse<List<RoomResponse>> findAll(){
        return ApiResponse.success(roomService.findAll());
    }
    @GetMapping("/{id}")
    public ApiResponse<RoomResponse> findById(@PathVariable long id){
        return roomService.findById(id).map(ApiResponse::success).orElseGet(()->ApiResponse.error("Not found", new RoomResponse()));
//       Optional<RoomResponse> room = roomService.findById(id);
//       if(room.isPresent()){
//           return ApiResponse.success(room.get());
//       }else {
//           return ApiResponse.error("Room not found", new RoomResponse());
//       }
    }
    @PostMapping
    public ApiResponse<RoomResponse> save(@RequestBody RoomUpsertRequest req) {
        return ApiResponse.success(roomService.save(req));
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<RoomResponse> update(@PathVariable long id,
                                            @RequestBody RoomUpsertRequest req) {
        return ApiResponse.success(roomService.save(id, req));
    }


}
