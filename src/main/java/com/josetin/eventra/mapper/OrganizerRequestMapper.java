package com.josetin.eventra.mapper;

import com.josetin.eventra.dto.response.OrganizerRequestResponse;
import com.josetin.eventra.entity.OrganizerRequest;
import org.springframework.stereotype.Component;

@Component
public class OrganizerRequestMapper {

    public OrganizerRequestResponse toResponse(OrganizerRequest orgRequest){
        return new OrganizerRequestResponse(
                orgRequest.getId(),
                orgRequest.getUser().getName(),
                orgRequest.getUser().getEmail(),
                orgRequest.getUser().getDepartment(),
                orgRequest.getUser().getDesignation(),
                orgRequest.getReason(),
                orgRequest.getStatus(),
                orgRequest.getRejectionReason(),
                orgRequest.getRequestedAt(),
                orgRequest.getResolvedAt()
        );
    }
}
