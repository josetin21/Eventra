package com.josetin.eventra.dto.response;

public record UserProfileResponse(
        String name,
        String email,
        String department,
        Integer year
) {
}
