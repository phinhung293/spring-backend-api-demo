package com.yo.day1.service.impl;

import com.yo.day1.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendAccountInfo(String toEmail, String username, String rawPassword) {
        if (toEmail == null || toEmail.isBlank()) {
            log.warn("Cannot send email, email address is empty for user: {}", username);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Thông tin tài khoản YOEDU Demo");
            
            String text = String.format("""
                    Chào bạn,
                    
                    Tài khoản của bạn đã được tạo thành công trên hệ thống YOEDU Demo.
                    Dưới đây là thông tin đăng nhập của bạn:
                    
                    - Tên đăng nhập: %s
                    - Mật khẩu: %s
                    
                    Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu để bảo đảm an toàn.
                    
                    Trân trọng,
                    YOEDU Demo Team
                    """, username, rawPassword);
                    
            message.setText(text);
            mailSender.send(message);
            
            log.info("Successfully sent account info email to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", toEmail, e);
        }
    }
}
