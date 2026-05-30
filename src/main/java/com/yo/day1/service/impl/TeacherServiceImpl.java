package com.yo.day1.service.impl;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Teacher;
import com.yo.day1.dto.teacher.TeacherResponse;
import com.yo.day1.dto.teacher.TeacherUpsertRequest;

import com.yo.day1.repository.TeacherRepository;
import com.yo.day1.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final ModelMapper mapper;

    @Override
    public List<TeacherResponse> getAll() {

        return teacherRepository.findAll()
                .stream()
                .map(teacher -> mapper.map(teacher, TeacherResponse.class))
                .toList();
    }

    @Override
    public TeacherResponse getById(Long id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        return mapper.map(teacher, TeacherResponse.class);
    }

    @Override
    public TeacherResponse create(TeacherUpsertRequest request) {

        if (teacherRepository.existsByTeacherCode(request.getTeacherCode())) {
            try {
                throw new BadRequestException("Teacher code already exists");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        if (teacherRepository.existsByPhone(request.getPhone())) {
            try {
                throw new BadRequestException("Phone already exists");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        if (request.getEmail() != null && teacherRepository.existsByEmail(request.getEmail())) {
            try {
                throw new BadRequestException("Email already exists");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        Teacher teacher = mapper.map(request, Teacher.class);

        teacherRepository.save(teacher);

        return mapper.map(teacher, TeacherResponse.class);
    }

    @Override
    public TeacherResponse update(Long id, TeacherUpsertRequest request) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        teacher.setTeacherCode(request.getTeacherCode());
        teacher.setFullName(request.getFullName());
        teacher.setPhone(request.getPhone());
        teacher.setEmail(request.getEmail());
        teacher.setTeacherRole(request.getTeacherRole());
        teacher.setCccdImageUrl(request.getCccdImageUrl());
        teacher.setIsActive(request.getIsActive());
        teacher.setNote(request.getNote());

        teacherRepository.save(teacher);

        return mapper.map(teacher, TeacherResponse.class);
    }

    @Override
    public void delete(Long id) {

        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found"));

        teacherRepository.delete(teacher);
    }
}