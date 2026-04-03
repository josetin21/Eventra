package com.josetin.eventra.service;

import com.josetin.eventra.dto.response.RegistrantResponse;
import com.josetin.eventra.entity.Event;
import com.josetin.eventra.entity.Registration;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.repository.EventRepository;
import com.josetin.eventra.repository.RegistrationRepository;
import com.josetin.eventra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventRegistrantService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    private Event getEventAndValidateOrganizer(Long eventId, Long organizerId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (event.getOrganizer() == null || !event.getOrganizer().getId().equals(organizerId)){
            throw new BusinessException("You are not authorized to access registrants for this event",HttpStatus.FORBIDDEN);
        }
        return event;
    }

    public List<RegistrantResponse> getRegistrantsForEvent(Long eventId){
        User currentUser = getCurrentUser();
        getEventAndValidateOrganizer(eventId, currentUser.getId());

        List<Registration> registrationList = registrationRepository.findByEventIdWithUser(eventId);

        return registrationList.stream()
                .map(registration -> {
                    User user = registration.getUser();
                    return new RegistrantResponse(
                            user.getName(),
                            user.getEmail(),
                            user.getDepartment(),
                            user.getYear(),
                            user.getDesignation() == null ? null : user.getDesignation().name()
                    );
                })
                .toList();
    }

}
