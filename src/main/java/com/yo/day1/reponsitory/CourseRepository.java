package com.yo.day1.reponsitory;

import com.yo.day1.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
