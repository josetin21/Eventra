package com.josetin.eventra.dto.response;

public record RegistrantResponse(
        String name,
        String email,
        String department,
        Integer year,
        String designation
) {}
