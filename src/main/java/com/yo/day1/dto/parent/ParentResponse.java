package com.yo.day1.dto.parent;

import com.yo.day1.domain.enums.Gender;
import lombok.Data;

@Data
public class ParentResponse {
    private  Long id;

    private String fullName;

    private String phone;

    private String email;

    private String address;

    private String relationship;

    private Gender gender = Gender.OTHER;
}