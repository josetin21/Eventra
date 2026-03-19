package com.josetin.eventra.dto.response;

import java.time.LocalDateTime;

public record AttendanceResponse(
        Long id,
        String eventTitle,
        String attendeeName,
        String sessionToken,
        LocalDateTime markedAt
) {
}
