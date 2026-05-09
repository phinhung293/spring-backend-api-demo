package com.yo.day1.dto.teacher;

import com.yo.day1.domain.enums.TeacherRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherUpsertRequest {

    @Size(min = 2)
    private String teacherCode;

    @Size(min = 2)
    private String fullName;

    @Pattern(regexp = "^(84|0[35789])+([0-9]{8})$")
    private String phone;

    @Email
    private String email;

    @NotNull
    private TeacherRole teacherRole = TeacherRole.TEACHER;

    private String cccdImageUrl;

    @NotNull
    private Boolean isActive = true;

    private String note;
}
