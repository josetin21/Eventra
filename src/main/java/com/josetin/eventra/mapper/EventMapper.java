package com.josetin.eventra.mapper;

import com.josetin.eventra.dto.response.EventResponse;
import com.josetin.eventra.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event){
        int registeredCount = event.getRegistrations() == null ? 0 : event.getRegistrations() .size();

        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getVenue(),
                event.getCapacity(),
                registeredCount,
                event.getRegistrationDeadline(),
                event.getOrganizer().getName(),
                event.getStatus(),
                event.getCreatedAt(),
                event.getIdCardUrl(),
                event.getPermissionLetterUrl(),
                event.getRejectionReason()
        );
    }
}
