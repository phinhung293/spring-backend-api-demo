package com.yo.day1.repository;

import com.yo.day1.domain.entity.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {
    List<CourseClass> findByNameContainingIgnoreCase(String keyword);
    java.util.List<com.yo.day1.domain.entity.CourseClass> findByStatus(com.yo.day1.domain.enums.ClassStatus status);
}
