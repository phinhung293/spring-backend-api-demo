package com.yo.day1.common;

import java.time.LocalDateTime;

public record ApiResponse <T>(boolean success, T data, String message, LocalDateTime timestamp) { // <T> generis: đại diện cho 1 kiểu ch biết, sau này có kiểu này chèn vô thì nó sẽ là <T>. Tự động ép kiểu (?)
    public static <T> ApiResponse <T> success(T data, String message) {
        return new ApiResponse<T>(true, data, message, LocalDateTime.now());
    }
    public static <T> ApiResponse <T> success(T data) {
        return success(data, "Success");
    }
    public  static ApiResponse <Void> successMessage(String message) {
        return success(null, message);
    }
    public  static ApiResponse <Void> error(String message) {
        return new ApiResponse<>(false,null, message,LocalDateTime.now());
    }
    public static <T> ApiResponse <T> error(String message, T errorData) {
        return new ApiResponse<>(false, errorData, message, LocalDateTime.now());
    }
    //dùng static vì k cần phải tạo đối tượng trc khi sài
}
