package com.josetin.eventra.dto.response;

public record AuthResponse(
        String token,
        String role,
        String name,
        String designation
) {
}
