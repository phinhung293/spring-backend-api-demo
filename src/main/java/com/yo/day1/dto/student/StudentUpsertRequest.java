package com.yo.day1.dto.student;

import com.yo.day1.domain.entity.Parent;
import com.yo.day1.domain.enums.Gender;
import com.yo.day1.domain.enums.StudentStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpsertRequest {

    @Size(min = 2)
    private String studentCode;

    @Size(min = 2)
    private String fullName;

    //K làm gtr MIN, MAX chỗ này đc,...
    private LocalDate dateOfBirth;

    @NotNull //Đảm bảo k null
    private Gender gender = Gender.OTHER;

    //    @Min(value = 1)
//    @Max(value = 12) //Tùy tiêu chuẩn
    //Số thì k dùng Size mà dùng MIN, MAX
//    @NotBlank //Kể cả khoảng trắng cx k cho
    @NotEmpty //khoảng trắng vẫn cho
    private String gradeLevel;

    private String schoolName;

    @Pattern(regexp = "^(84|0[35789])+([0-9]{8})$") //của Việt Nam
    private String phone;

    @Min(value = 0)
    private Long parentId; //Sửa phần này: Chỉ cần id là đủ r
    //Khóa ngoại
    //K có parentID được không? -> Tùy theo quy định (vd: Đại học thì đc, còn c3 thì k nên )

    private StudentStatus status = StudentStatus.ACTIVE;

    @Min(value = 0)
    @Max(value = 10) //Tùy thang điểm
    private BigDecimal latestScore = BigDecimal.ZERO;

    private String note;

//    private LocalDateTime createdAt;
//    private LocalDateTime updateAt;
// K có 2 cái này do server tự tạo phần này nên k truyền lên đc

}
