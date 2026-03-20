package com.josetin.eventra.service;

import com.josetin.eventra.dto.response.DashboardResponse;
import com.josetin.eventra.dto.response.OrganizerDashboardResponse;
import com.josetin.eventra.entity.EventStatus;
import com.josetin.eventra.entity.RequestStatus;
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
    private final OrganizerRequestRepository organizerRequestRepository;

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
                organizerRequestRepository.countByStatus (RequestStatus.PENDING),
                eventRepository.countByStatus (EventStatus.ACTIVE),
                eventRepository.countByStatus(EventStatus.CANCELLED)
        );
    }

    public OrganizerDashboardResponse getOrganizerDashboard(){
        User organizer = getCurrentUser();

        return new OrganizerDashboardResponse(
                eventRepository.countByOrganizerId(organizer.getId()),
                eventRepository.countByOrganizerIdAndStatus(organizer.getId(), EventStatus.ACTIVE),
                registrationRepository.countByEventOrganizerId(organizer.getId()),
                attendanceRepository.countByEventOrganizerId(organizer.getId()),
                eventRepository.countByOrganizerIdAndStatus(organizer.getId(),EventStatus.CANCELLED)
        );
    }

}
