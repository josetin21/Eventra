package com.josetin.eventra.service;

import com.josetin.eventra.dto.response.AttendanceSessionResponse;
import com.josetin.eventra.entity.AttendanceSession;
import com.josetin.eventra.entity.Event;
import com.josetin.eventra.entity.EventStatus;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.mapper.AttendanceMapper;
import com.josetin.eventra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    public AttendanceSessionResponse openSession(Long eventId, int durationMinutes){
        User organizer = getCurrentUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getId().equals(organizer.getId())){
            throw new BusinessException("You are not the organizer of this event", HttpStatus.FORBIDDEN);
        }

        if (event.getStatus() != EventStatus.ACTIVE){
            throw new BusinessException("Event is not active", HttpStatus.BAD_REQUEST)
        }

        AttendanceSession session = AttendanceSession.builder()
                .event(event)
                .createdBy(organizer)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(durationMinutes))
                .build();

        AttendanceSession saved = attendanceSessionRepository.save(session);
        return attendanceMapper.toSessionResponse(saved);
    }
}
