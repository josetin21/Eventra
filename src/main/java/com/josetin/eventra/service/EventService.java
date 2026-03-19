package com.josetin.eventra.service;

import com.josetin.eventra.dto.request.EventRequest;
import com.josetin.eventra.dto.response.EventResponse;
import com.josetin.eventra.entity.Event;
import com.josetin.eventra.entity.EventStatus;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.mapper.EventMapper;
import com.josetin.eventra.repository.EventRepository;
import com.josetin.eventra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    public EventResponse createEvent(EventRequest request){
        User organizer = getCurrentUser();

        Event event = Event.builder()
                .title(request.title())
                .description(request.description())
                .eventDate(request.eventDate())
                .venue(request.venue())
                .capacity(request.capacity())
                .registrationDeadline(request.registrationDeadline())
                .organizer(organizer)
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }

    public List<EventResponse> getAllEvents(){
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public EventResponse getEventById(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Event not found", HttpStatus.NOT_FOUND));
        return eventMapper.toResponse(event);
    }

    public EventResponse updateEvent(Long id, EventRequest request){
        User currentUser = getCurrentUser();
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if(!event.getOrganizer().getId().equals(currentUser.getId())){
            throw new BusinessException("You are not authorized to edit this event",HttpStatus.FORBIDDEN);
        }

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setVenue(request.venue());
        event.setCapacity(request.capacity());
        event.setRegistrationDeadline(request.registrationDeadline());


        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }

    public void cancelEvent(Long id){
        User currentUser = getCurrentUser();
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getId().equals(currentUser.getId())){
            throw new BusinessException("You are not authorized to edit this event", HttpStatus.FORBIDDEN);
        }

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }


}
