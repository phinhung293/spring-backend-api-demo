package com.yo.day1.controller;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.dto.courseclass.CourseClassResponse;
import com.yo.day1.dto.courseclass.CourseClassUpsertRequest;
import com.yo.day1.service.CourseClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course-classes")
@RequiredArgsConstructor
public class CourseClassController {

    private final CourseClassService courseClassService;

    public ResponseEntity<List<CourseClassResponse>> findAll() {

        List<CourseClassResponse> result = courseClassService.findAll();

        return ResponseEntity.ok(result);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<CourseClassResponse> findById(
            @PathVariable Long id
    ) throws NotFoundException {

        Optional<CourseClassResponse> result = courseClassService.findById(id);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }

        throw new NotFoundException("Course class not found");
    }

    @PostMapping
    public ResponseEntity<CourseClassResponse> create(
            @RequestBody CourseClassUpsertRequest req
    ) {

        CourseClassResponse result = courseClassService.create(req);

        return ResponseEntity.ok(result);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<CourseClassResponse> update(
            @PathVariable Long id,
            @RequestBody CourseClassUpsertRequest req
    ) throws NotFoundException {

        CourseClassResponse result = courseClassService.update(id, req);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id
    ) throws NotFoundException {

        courseClassService.delete(id);

        return ResponseEntity.ok("Delete successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseClassResponse>> search(
            @RequestParam String keyword
    ) {

        List<CourseClassResponse> result =
                courseClassService.search(keyword);

        return ResponseEntity.ok(result);
    }
}
