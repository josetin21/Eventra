package com.josetin.eventra.service;

import com.josetin.eventra.dto.request.UpdateProfileRequest;
import com.josetin.eventra.dto.response.UserProfileResponse;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.mapper.UserMapper;
import com.josetin.eventra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    public UserProfileResponse getMyProfile(){
        User user = getCurrentUser();
        return userMapper.toResponse(user);
    }

    public UserProfileResponse updateMyProfile(UpdateProfileRequest request){
        User user = getCurrentUser();

        user.setName(request.name());
        user.setYear(request.year());

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }



}
