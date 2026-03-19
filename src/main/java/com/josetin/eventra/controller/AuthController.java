package com.josetin.eventra.controller;

import com.josetin.eventra.dto.request.AdminRegisterRequest;
import com.josetin.eventra.dto.request.LoginRequest;
import com.josetin.eventra.dto.request.RegisterRequest;
import com.josetin.eventra.dto.response.AuthResponse;
import com.josetin.eventra.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

}
