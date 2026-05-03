package com.yo.day1.common;

import java.time.LocalDateTime;

public record ApiResponse<T>(boolean success, String message, T data, LocalDateTime timestamp) {
//    Generics là việc dùng "biến đại diện" cho kiểu dữ liệu.
//    Bình thường: Bạn phải chỉ định rõ là String, Integer hay Student. (Giống như cái hộp chỉ đựng được táo).
//    Generics (<T>): Bạn để một "chỗ trống" đại diện là T. (Giống như một cái thùng đa năng, lúc dùng bạn muốn bỏ táo, cam hay dưa hấu vào đều được).
    public static <T> ApiResponse<T> success(String message,T data) {
        return new ApiResponse<>(true, message, data,LocalDateTime.now());
    }
    public static <T> ApiResponse<T> success(T data) {
        return success("success", data);
    }
    public static ApiResponse<Void> successMessage(String message) {
        return success(message, null);
    }
    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false,message,null,LocalDateTime.now());
    }
}
