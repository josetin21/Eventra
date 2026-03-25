package com.josetin.eventra.mapper;

import com.josetin.eventra.dto.response.UserProfileResponse;
import com.josetin.eventra.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileResponse toResponse(User user){
        return new UserProfileResponse(
                user.getName(),
                user.getEmail(),
                user.getDepartment(),
                user.getYear()
        );
    }
}
