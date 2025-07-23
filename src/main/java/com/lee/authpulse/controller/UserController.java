package com.lee.authpulse.controller;

import com.lee.authpulse.io.ProfileRequest;
import com.lee.authpulse.io.ProfileResponse;
import com.lee.authpulse.service.MailService;
import com.lee.authpulse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final MailService mailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = userService.register(request);
        //Welcome email message
        mailService.welcomeEmail(response.getEmail(), response.getUsername());

        return response;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return userService.getProfile(email);
    }
}
