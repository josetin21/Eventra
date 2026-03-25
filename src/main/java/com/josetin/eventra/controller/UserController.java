package com.josetin.eventra.controller;

import com.josetin.eventra.dto.request.UpdateProfileRequest;
import com.josetin.eventra.dto.response.UserProfileResponse;
import com.josetin.eventra.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(){
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request){
        return ResponseEntity.ok(userService.updateMyProfile(request));
    }

}
