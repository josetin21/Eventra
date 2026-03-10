package com.josetin.eventra.service;

import com.josetin.eventra.dto.request.RegisterRequest;
import com.josetin.eventra.entity.Role;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request){
        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.STUDENT)
                .department(request.department())
                .year(request.year())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}
