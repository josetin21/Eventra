package com.josetin.eventra.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
