package com.josetin.eventra.service;

import com.josetin.eventra.dto.request.EventRequest;
import com.josetin.eventra.dto.response.EventResponse;
import com.josetin.eventra.entity.Event;
import com.josetin.eventra.entity.EventStatus;
import com.josetin.eventra.entity.Registration;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.mapper.EventMapper;
import com.josetin.eventra.repository.EventRepository;
import com.josetin.eventra.repository.RegistrationRepository;
import com.josetin.eventra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final RegistrationRepository registrationRepository;
    private final EmailService emailService;

    private boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    public EventResponse createEvent(EventRequest request){
        if (isBlank(request.idCardUrl()) && isBlank(request.permissionLetterUrl())){
            throw new BusinessException("At least one verification document is required", HttpStatus.NOT_FOUND);
        }

        User organizer = getCurrentUser();

        Event event = Event.builder()
                .title(request.title())
                .description(request.description())
                .eventDate(request.eventDate())
                .venue(request.venue())
                .capacity(request.capacity())
                .registrationDeadline(request.registrationDeadline())
                .idCardUrl(request.idCardUrl())
                .permissionLetterUrl(request.permissionLetterUrl())
                .organizer(organizer)
                .status(EventStatus.PENDING_APPROVAL)
                .createdAt(LocalDateTime.now())
                .build();

        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }

    public List<EventResponse> getAllEvents(){
        return eventRepository.findByStatus(EventStatus.APPROVED)
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public List<EventResponse> getPendingEvents(){
        return eventRepository.findByStatus(EventStatus.PENDING_APPROVAL)
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public List<EventResponse> getMyEvents(){
        User currentUser = getCurrentUser();
        return eventRepository.findByOrganizerId(currentUser.getId())
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public EventResponse approveEvent(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (event.getStatus() != EventStatus.PENDING_APPROVAL){
            throw new BusinessException("Event is not pending for approval", HttpStatus.BAD_REQUEST);
        }

        event.setStatus(EventStatus.APPROVED);
        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }

    public EventResponse rejectEvent(Long id, String reason){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (event.getStatus() != EventStatus.PENDING_APPROVAL){
            throw new BusinessException("Event is not pending for approval", HttpStatus.BAD_REQUEST);
        }

        event.setStatus(EventStatus.REJECTED);
        event.setRejectionReason(reason);
        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
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

        if (!event.getOrganizer().getId().equals(currentUser.getId())){
            throw new BusinessException("You are not authorized to update this event", HttpStatus.FORBIDDEN);
        }

        if (event.getStatus() != EventStatus.APPROVED){
            throw new BusinessException("Only approved events can be edited", HttpStatus.BAD_REQUEST);
        }

        boolean notify = request.notifyRegistrants() == null || request.notifyRegistrants();

        LocalDateTime oldEventDate = event.getEventDate();
        String oldVenue = event.getVenue();

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setVenue(request.venue());
        event.setCapacity(request.capacity());
        event.setRegistrationDeadline(request.registrationDeadline());

        Event saved = eventRepository.save(event);

        boolean importantChange = (
                oldEventDate != null && !oldEventDate.equals(saved.getEventDate()))
                        || (oldVenue != null && !oldVenue.equals(saved.getVenue()));

        if (notify && importantChange){
            List<Registration> registrations = registrationRepository.findByEventId(saved.getId());

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            String oldDateFormatted = oldEventDate != null ? oldEventDate.format(fmt) : "-";
            String newDateFormatted = saved.getEventDate() != null ? saved.getEventDate().format(fmt) : "-";

            for (Registration r : registrations){
                String toEmail = r.getUser().getEmail();
                String studentName = r.getUser().getName();

                emailService.sendEventUpdateNotification(
                        toEmail,
                        studentName,
                        saved.getTitle(),
                        oldVenue,
                        saved.getVenue(),
                        oldDateFormatted,
                        newDateFormatted
                );
            }
        }

        return eventMapper.toResponse(saved);
    }

    public void cancelEvent(Long id){
        User currentUser = getCurrentUser();
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getId().equals(currentUser.getId())){
            throw new BusinessException("You are not authorized to update this event", HttpStatus.FORBIDDEN);
        }

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }


}
