package com.josetin.eventra.controller;


import com.josetin.eventra.dto.response.RegistrationResponse;
import com.josetin.eventra.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("events/{eventId}")
    public ResponseEntity<RegistrationResponse> register(@PathVariable Long eventId){
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.register(eventId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RegistrationResponse>> getMyRegistration(){
        return ResponseEntity.ok(registrationService.getMyRegistration());
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<String> cancelRegistration(@PathVariable Long registrationId){
        registrationService.cancelRegistration(registrationId);
        return ResponseEntity.ok("Registration cancelled successfully");
    }
}
