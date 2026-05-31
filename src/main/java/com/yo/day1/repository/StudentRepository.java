package com.yo.day1.repository;

import com.yo.day1.domain.entity.Student;
import com.yo.day1.domain.enums.StudentStatus;
import com.yo.day1.dto.student.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {//dùng class sẽ sai vì spring boot sinh mã cho mình
    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :search, '%'))")
    java.util.List<Student> searchByCodeOrName(@Param("search") String search);
    java.util.List<Student> findByStatus(com.yo.day1.domain.enums.StudentStatus status);
    boolean existsByStudentCode(String studentCode);
    boolean existsByStudentCodeAndIdNot(String studentCode, Long id);
    List<Student> findByParentId(Long parentId);
}
