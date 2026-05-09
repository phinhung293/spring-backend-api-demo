package com.yo.day1.service;

import com.yo.day1.dto.teacher.TeacherResponse;
import com.yo.day1.dto.teacher.TeacherUpsertRequest;

import java.util.List;

public interface TeacherService {

    List<TeacherResponse> getAll();

    TeacherResponse getById(Long id);

    TeacherResponse create(TeacherUpsertRequest request);

    TeacherResponse update(Long id, TeacherUpsertRequest request);

    void delete(Long id);
}