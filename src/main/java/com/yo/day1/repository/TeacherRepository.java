package com.yo.day1.repository;

import com.yo.day1.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByTeacherCode(String teacherCode);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}