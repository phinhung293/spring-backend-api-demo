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
    @org.springframework.security.access.prepost.PreAuthorize(
            "hasAnyRole('ADMIN', 'ACADEMIC_STAFF') or " +
                    "(hasRole('PARENT') and @studentServiceImpl.getStudent(#id).parent != null and " +
                    "principal.username == @studentServiceImpl.getStudent(#id).parent.user.username)"
    )
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
    // Test tìm kiếm: GET http://localhost:8080/api/students/search?search=Name
    @GetMapping(value = "/search")
    public ResponseEntity<List<StudentResponse>> search(@RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(studentService.findAll(search));
    }
    // Test lọc trạng thái: GET http://localhost:8080/api/students/status/ACTIVE
    @GetMapping(value = "/status/{status}")
    public ResponseEntity<List<StudentResponse>> findByStatus(@PathVariable("status") com.yo.day1.domain.enums.StudentStatus status) {
        return ResponseEntity.ok(studentService.findByStatus(status));
    }
}
