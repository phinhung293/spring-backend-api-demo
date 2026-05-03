package com.yo.day1.domain.enums;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
public class AuditableEntity {//Kiểm soát ghi nhận ngày nhận ngày gửi
    @CreationTimestamp
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;//sinh ra 1 lần khi dữ liệu được tạo ra

    @UpdateTimestamp
    @Column(name="updated_at",nullable = false)
    private LocalDateTime updatedAt;
}
