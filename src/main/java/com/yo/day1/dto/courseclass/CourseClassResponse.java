package com.yo.day1.dto.courseclass;

import com.yo.day1.domain.entity.Course;
import com.yo.day1.domain.entity.Room;
import com.yo.day1.domain.entity.ScheduleSlot;
import com.yo.day1.domain.entity.Teacher;
import com.yo.day1.domain.enums.ClassStatus;
import com.yo.day1.dto.course.CourseResponse;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
public class CourseClassResponse {
    private Long id;
    private String codeCode;

    private String name;


    private CourseResponse course;

    private Room room;

    private ScheduleSlot slot;

    private Teacher mainteacher;

    private Teacher assistantteacher;

    private LocalDate startDate;
    private LocalDate endDate;

    private int maxStudents;

    private double tuitionFee;

    private ClassStatus status = ClassStatus.OPEN;
}
