package com.josetin.eventra.dto.response;

import com.josetin.eventra.entity.Designation;
import com.josetin.eventra.entity.RequestStatus;

import java.time.LocalDateTime;

public record OrganizerRequestResponse(
        Long id,
        String userName,
        String userEmail,
        String department,
        Designation designation,
        String reason,
        RequestStatus status,
        String rejectionReason,
        LocalDateTime requestedAt,
        LocalDateTime resolvedAt
) {
}
