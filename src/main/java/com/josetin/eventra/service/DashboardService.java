package com.josetin.eventra.service;

import com.josetin.eventra.dto.response.DashboardResponse;
import com.josetin.eventra.entity.EventStatus;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final AttendanceRepository attendanceRepository;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    public DashboardResponse getDashboard(){
        return new DashboardResponse(
                userRepository.count(),
                eventRepository.count(),
                registrationRepository.count(),
                attendanceRepository.count(),
                eventRepository.countByStatus (EventStatus.PENDING_APPROVAL),
                eventRepository.countByStatus (EventStatus.APPROVED),
                eventRepository.countByStatus(EventStatus.REJECTED)
        );
    }


}
