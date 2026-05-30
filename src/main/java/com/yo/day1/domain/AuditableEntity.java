package com.yo.day1.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
public class AuditableEntity extends BaseEntity {//Kiểm soát ghi nhận ngày nhận ngày gửi
    @CreationTimestamp
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;//sinh ra 1 lần khi dữ liệu được tạo ra

    @UpdateTimestamp
    @Column(name="updated_at",nullable = false)
    private LocalDateTime updatedAt;
    //Annotation: Lần đầu tạo bản ghi
    @PrePersist
    private void save() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    //Chỉnh sửa bản ghi
    @PreUpdate
    private void update() {
        updatedAt = LocalDateTime.now();
    }
}
