package com.yo.day1.service;

public interface EmailService {
    void sendAccountInfo(String toEmail, String username, String rawPassword);
}
