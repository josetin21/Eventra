package com.josetin.eventra.dto.response;

import com.josetin.eventra.entity.EventStatus;

import java.time.LocalDateTime;

public record EventResponse(

        Long id,
        String title,
        String description,
        LocalDateTime eventDate,
        String venue,
        Integer capacity,
        Integer registeredCount,
        LocalDateTime registrationDeadline,
        EventStatus status,
        LocalDateTime createdAt,
        String idCardUrl,
        String permissionLetterUrl,
        String rejectionReason
) {
}
