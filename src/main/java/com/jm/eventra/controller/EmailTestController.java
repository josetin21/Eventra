package com.jm.eventra.controller;

import com.jm.eventra.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    public ResponseEntity<String> testEmail() {
        try {
            emailService.sendPasswordResetOtp("josetin2001@gmail.com", "123456");
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage() + "\nCause: " + e.getCause());
        }
    }
}