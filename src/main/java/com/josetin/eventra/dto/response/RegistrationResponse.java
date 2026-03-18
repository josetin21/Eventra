package com.josetin.eventra.dto.response;

import java.time.LocalDateTime;

public record RegistrationResponse(
        Long id,
        String eventTitle,
        String eventVenue,
        LocalDateTime eventDate,
        String attendanceName,
        String qrCode,
        LocalDateTime registeredAt
) {
}
