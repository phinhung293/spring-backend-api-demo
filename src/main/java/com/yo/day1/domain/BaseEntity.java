package com.yo.day1.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass //dùng để định nghĩa các thuộc tính chung (như ID, ngày tạo) cho nhiều Entity kế thừa mà không cần tạo bảng riêng cho lớp cha đó.
@Setter
@Getter
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//trong lt hướng đối tượng quyền sử dụng field luôn là private

}
