package com.yo.day1.controller;

import com.yo.day1.dto.teacher.TeacherResponse;
import com.yo.day1.dto.teacher.TeacherUpsertRequest;
import com.yo.day1.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;



    @GetMapping("/{id}")
    public TeacherResponse getById(@PathVariable Long id) {
        return teacherService.getById(id);
    }

    @PostMapping
    public TeacherResponse create(@Valid @RequestBody TeacherUpsertRequest request) {
        return teacherService.create(request);
    }

    @PutMapping("/{id}")
    public TeacherResponse update(@PathVariable Long id, @Valid @RequestBody TeacherUpsertRequest request) {
        return teacherService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }
    //GET /api/teachers?active=true
    @GetMapping
    public ResponseEntity<List<TeacherResponse>> getTeachers(
            @RequestParam(value = "active", required = false) Boolean active) {

        // Nếu người dùng truyền param ?active=true hoặc ?active=false
        if (active != null) {
            com.yo.day1.domain.enums.TeacherStatus status = active ?
                    com.yo.day1.domain.enums.TeacherStatus.ACTIVE :
                    com.yo.day1.domain.enums.TeacherStatus.INACTIVE;
            return ResponseEntity.ok(teacherService.findByStatus(status));
        }

        // Nếu không truyền param (ví dụ gọi /api/teachers) -> Trả về tất cả như hàm getAll() cũ
        return ResponseEntity.ok(teacherService.getAll());
    }
}