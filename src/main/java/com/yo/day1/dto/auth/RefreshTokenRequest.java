package com.yo.day1.dto.auth;

import jakarta.validation.constraints.NotBlank;
//Sử dụng record thay vì class vì xử lí xây dựng, tiết kiệm bộ nhớ tạo đối tượng ko thay đổi được đỡ tốn CPU ram để theo dõi
public record RefreshTokenRequest(
        @NotBlank String refreshToken) {

}
