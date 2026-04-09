package com.jm.eventra.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
