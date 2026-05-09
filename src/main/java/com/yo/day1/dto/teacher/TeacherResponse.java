package com.yo.day1.dto.teacher;

import com.yo.day1.domain.enums.TeacherRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {

    private Long id;

    private String teacherCode;

    private String fullName;

    private String phone;

    private String email;

    private TeacherRole teacherRole;

    private String cccdImageUrl;

    private Boolean isActive;

    private String note;
}
