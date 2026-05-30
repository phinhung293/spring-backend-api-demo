package com.yo.day1.controller;

import com.yo.day1.dto.parent.ParentResponse;
import com.yo.day1.dto.parent.ParentUpsertRequest;
import com.yo.day1.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    //Lấy toàn bộ parent
    @GetMapping
    public List<ParentResponse> getAll() {

        return parentService.getAll();
    }

    //Lấy parent theo id
    @GetMapping("/{id}")
    public ParentResponse getById(@PathVariable Long id) {

        return parentService.getById(id);
    }

    //Tạo parent mới
    @PostMapping
    public ParentResponse create(@Valid @RequestBody ParentUpsertRequest request) {

        return parentService.create(request);
    }

    //Update parent
    @PutMapping("/{id}")
    public ParentResponse update(@PathVariable Long id, @Valid @RequestBody ParentUpsertRequest request) {

        return parentService.update(id, request);
    }

    //Xóa parent
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        parentService.delete(id);
    }
}