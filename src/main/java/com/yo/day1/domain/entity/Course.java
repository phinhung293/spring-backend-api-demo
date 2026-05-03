package com.yo.day1.domain.entity;

import com.yo.day1.domain.enums.AuditableEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Course extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "varchar(20)")
    private String courseCode;
    @Column(columnDefinition = "varchar(100)")
    private String name;
    @Column(columnDefinition = "text")
    private String description;

    private double tuitionFee;

    private int totalSessions;

    private byte isActive;
}
