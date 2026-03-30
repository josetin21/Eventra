package com.josetin.eventra.mapper;

import com.josetin.eventra.dto.response.RegistrationResponse;
import com.josetin.eventra.entity.Registration;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper {

    public RegistrationResponse toResponse(Registration registration){
        return new RegistrationResponse(
                registration.getId(),
                registration.getEvent().getTitle(),
                registration.getEvent().getVenue(),
                registration.getEvent().getEventDate(),
                registration.getUser().getName(),
                registration.getQrCode(),
                registration.getQrContent(),
                registration.getRegisteredAt()
        );
    }
}
