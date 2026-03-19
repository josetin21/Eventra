package com.josetin.eventra.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OrganizerRequestCreate(

        @NotBlank(message = "Reason is required")
        String reason
) {
}
