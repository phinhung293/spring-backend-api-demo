package com.yo.day1.controller;

import com.yo.day1.domain.entity.Student;
import com.yo.day1.reponsitory.StudentRepository;
import com.yo.day1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    @GetMapping
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("\"data\"This is my content\"");
    }
    @GetMapping(value = "/students")
    public ResponseEntity<List<Student>> findAll(){
        return ResponseEntity.ok(studentService.findByAll());
    }

}
