package com.josetin.eventra.dto.response;

public record UserProfileResponse(
        String name,
        String email,
        String institutionName,
        String department,
        Integer year
) {
}
