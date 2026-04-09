package com.jm.eventra.controller;

import com.jm.eventra.dto.request.*;
import com.jm.eventra.dto.request.*;
import com.jm.eventra.dto.response.AuthResponse;
import com.jm.eventra.service.AuthService;
import com.jm.eventra.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody AdminRegisterRequest request){
        authService.registerAdmin(request);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/forgot-password/otp")
    public ResponseEntity<?> requestPasswordResetOtp(@Valid @RequestBody ForgotPasswordOtpRequest request){
        try{
            passwordResetService.requestOtp(request.email());
        } catch (Exception ignored){

        }
        return ResponseEntity.ok(Map.of("message", "An OTP has been sent."));
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPasswordWithOtp(@Valid @RequestBody ResetPasswordWithOtpRequest request){
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message","password reset successful. Please login."));
    }

}
