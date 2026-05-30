package com.yo.day1.service;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.CourseClass;
import com.yo.day1.domain.enums.ClassStatus;
import com.yo.day1.dto.courseclass.CourseClassResponse;
import com.yo.day1.dto.courseclass.CourseClassUpsertRequest;

import java.util.List;
import java.util.Optional;

public interface CourseClassService {
    List<CourseClassResponse> findAll();

    Optional<CourseClassResponse> findById(Long id);

    CourseClassResponse create(CourseClassUpsertRequest req);

    CourseClassResponse update(Long id, CourseClassUpsertRequest req) throws NotFoundException;

    void delete(Long id) throws NotFoundException;
    CourseClass getCourseClass(Long id);

    List<CourseClassResponse> search(String keyword);
    java.util.List<CourseClassResponse> findByStatus(ClassStatus status);
}
