package com.lee.authpulse.service;

import com.lee.authpulse.entity.Role;
import com.lee.authpulse.entity.User;
import com.lee.authpulse.io.ProfileRequest;
import com.lee.authpulse.io.ProfileResponse;
import com.lee.authpulse.repository.UserRepository;
import com.lee.authpulse.utils.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final PasswordValidator passwordValidator;

    @Override
    public ProfileResponse register(ProfileRequest request) {
        // Validate password strength
        var validationResult = passwordValidator.validatePassword(request.getPassword());
        if (!validationResult.isValid()) {
            throw new RuntimeException("Password validation failed: " + String.join(", ", validationResult.errors()));
        }

        User newUser = convertToUser(request);
        newUser = repository.save(newUser);
        return convertToResponse(newUser);
    }

    @Override
    public ProfileResponse getProfile(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email:" + email + "not found"));
        return convertToResponse(user);
    }

    @Override
    public void sendResetOtp(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        user.setResetOtp(otp);
        user.setResetOtpExpireAt(System.currentTimeMillis() + 15 * 60 * 1000); // OTP valid for 15 minutes
        repository.save(user);
        try {
            mailService.sendResetPassword(user.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        // Validate password strength
        var validationResult = passwordValidator.validatePassword(newPassword);
        if (!validationResult.isValid()) {
            throw new RuntimeException("Password validation failed: " + String.join(", ", validationResult.errors()));
        }

        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        if (user.getResetOtp() == null || !user.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getResetOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setResetOtpExpireAt(0L);
        repository.save(user);
    }

    @Override
    public void getUserId(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        System.out.println("User ID: " + user.getUserid());

    }

    @Override
    public void verifyOtp(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        user.setVerifyOtp(otp);
        user.setVerifyOtpExpireAt(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // OTP valid for 24 hours
        repository.save(user);

        try {
            mailService.sendVerifyOtp(user.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void verifyEmail(String email, String otp) {
        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        if (user.getVerifyOtp() == null || !user.getVerifyOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        if (user.getVerifyOtpExpireAt()<System.currentTimeMillis()) {
            throw new RuntimeException("OTP expired");
        }
        user.setAccountVerified(true);
        user.setVerifyOtp(null);
        user.setVerifyOtpExpireAt(0L);
        repository.save(user);

    }

    private User convertToUser(ProfileRequest request) {
        return User.builder()
                .userid(UUID.randomUUID().toString())
                .name(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .role(Role.USER)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .build();
    }

    private ProfileResponse convertToResponse(User user) {
        return ProfileResponse.builder()
                .username(user.getName())
                .userid(user.getUserid())
                .email(user.getEmail())
                .isAccountVerified(user.isAccountVerified())
                .build();
    }
}
