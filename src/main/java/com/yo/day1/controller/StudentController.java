package com.yo.day1.controller;

import com.yo.day1.dto.student.StudentResponse;
import com.yo.day1.dto.student.StudentUpsertRequest;
import com.yo.day1.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor //Lombok
@RequestMapping(value = "/api/students")
public class StudentController {
    private final StudentService studentService;

//    @GetMapping
//    public ResponseEntity<String> home() {
//        return ResponseEntity.ok("\"data\":\"This is my content\""); //dấu \ trước nháy đôi
//    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.findByAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable long id) {
        //return ResponseEntity.ok(studentService.findById(id));

        //do là optional
        return studentService.findById(id)
                //.map(ResponseEntity::ok)
                .map(stu-> ResponseEntity.ok(stu))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<StudentResponse> create(@Valid
                                           @RequestBody StudentUpsertRequest req) {
        return ResponseEntity.ok(studentService.create(req));
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<StudentResponse> update(@PathVariable Long id, StudentUpsertRequest req) {
        return ResponseEntity.ok(studentService.update(id, req));
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();

    }
}
