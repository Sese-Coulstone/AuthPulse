package com.lee.authpulse.service;

import com.lee.authpulse.io.ProfileRequest;
import com.lee.authpulse.io.ProfileResponse;

public interface UserService {
    ProfileResponse register(ProfileRequest request);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String email, String otp, String newPassword);
    void getUserId(String email);
    void verifyOtp(String email);
    void verifyEmail(String email, String otp);
}
