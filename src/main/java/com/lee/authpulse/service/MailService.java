package com.lee.authpulse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${MAIL_FROM}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public void welcomeEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to AuthPulse");
        message.setText("Hello " + username + ",\n\n" +
                "Welcome to AuthPulse! We are excited to have you on board.\n\n" +
                "Best regards,\n" +
                "The AuthPulse Team");
        mailSender.send(message);
    }

    public void sendResetPassword(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Hello,\n\n"+
                "You have requested to reset your password. Please use the following token to reset your password:\n" +
                "Token: " + token + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The AuthPulse Team");
        mailSender.send(message);

    }

    public void sendVerifyOtp(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Verify Your Email");
        message.setText("Hello,\n\n" +
                "Please use the following OTP to verify your email:\n" +
                "OTP: " + token + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The AuthPulse Team");
        mailSender.send(message);
    }
}
