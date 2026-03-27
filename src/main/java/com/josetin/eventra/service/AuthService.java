package com.josetin.eventra.service;

import com.josetin.eventra.dto.request.AdminRegisterRequest;
import com.josetin.eventra.dto.request.LoginRequest;
import com.josetin.eventra.dto.request.RegisterRequest;
import com.josetin.eventra.dto.response.AuthResponse;
import com.josetin.eventra.entity.Designation;
import com.josetin.eventra.entity.Role;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.repository.UserRepository;
import com.josetin.eventra.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.admin.key}")
    private String adminKey;

    public void register(RegisterRequest request){
        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new BusinessException("Email already registered", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .designation(request.designation())
                .department(request.department())
                .year(request.year())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public void registerAdmin(AdminRegisterRequest request){
        if (!request.adminKey().equals(adminKey)){
            throw new BusinessException("Invalid admin Key", HttpStatus.FORBIDDEN);
        }

        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new BusinessException("Email already registered", HttpStatus.CONFLICT);
        }

        User admin = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ADMIN)
                .designation(Designation.FACULTY)
                .department("Administration")
                .year(0)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new BusinessException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new BusinessException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getName()
        );
    }
}
