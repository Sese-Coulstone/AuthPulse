package com.lee.authpulse.io;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "OTP is required")
    private String otp;
    @NotNull(message = "New password is required")
    private String newPassword;
}
