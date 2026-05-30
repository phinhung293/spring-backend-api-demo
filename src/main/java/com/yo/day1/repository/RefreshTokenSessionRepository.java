package com.yo.day1.repository;

import com.yo.day1.domain.entity.RefreshTokenSession;
import com.yo.day1.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenSessionRepository extends JpaRepository<RefreshTokenSession, Long> {

    Optional<RefreshTokenSession> findByJti(String jti);//Tìm theo token theo chuỗi, token đầu tiên (không tìm theo khóa chính)
    //Findby + tên field
    //Optional<User> findByUser_Username(String username); //Tìm theo user name
    List<RefreshTokenSession> findByUserIdAndRevokedAtIsNull(Long userId);//Lấy về refresh chưa bị thu hồi

    List<RefreshTokenSession> findByExpiresAtBeforeAndRevokedAtIsNull(Instant now);//Lấy về các token chưa hết hạn và chưa thu hồi
}

