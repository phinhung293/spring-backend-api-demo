package com.yo.day1.reponsitory;

import com.yo.day1.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {//dùng class sẽ sai vì spring boot sinh mã cho mình

}
