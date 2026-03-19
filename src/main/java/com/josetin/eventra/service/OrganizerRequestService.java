package com.josetin.eventra.service;

import com.josetin.eventra.dto.request.OrganizerRequestCreate;
import com.josetin.eventra.dto.response.OrganizerRequestResponse;
import com.josetin.eventra.entity.OrganizerRequest;
import com.josetin.eventra.entity.RequestStatus;
import com.josetin.eventra.entity.Role;
import com.josetin.eventra.entity.User;
import com.josetin.eventra.exception.BusinessException;
import com.josetin.eventra.mapper.OrganizerRequestMapper;
import com.josetin.eventra.repository.OrganizerRequestRepository;
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
public class OrganizerRequestService {

    private final OrganizerRequestRepository organizerRequestRepository;
    private final UserRepository userRepository;
    private final OrganizerRequestMapper organizerRequestMapper;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    public OrganizerRequestResponse submitRequest(OrganizerRequestCreate request){
        User user = getCurrentUser();

        if (user.getRole() == Role.ORGANIZER || user.getRole() == Role.ADMIN){
            throw new BusinessException("You are already an organizer or admin", HttpStatus.BAD_REQUEST);
        }

        if (organizerRequestRepository.existsByUserIdAndStatus(user.getId(), RequestStatus.PENDING)){
            throw new BusinessException("You already have a pending request", HttpStatus.CONFLICT);
        }

        OrganizerRequest orgRequest = OrganizerRequest.builder()
                .user(user)
                .reason(request.reason())
                .status(RequestStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        OrganizerRequest saved = organizerRequestRepository.save(orgRequest);
        return organizerRequestMapper.toResponse(saved);
    }

    public List<OrganizerRequestResponse> getPendingRequests(){
        return organizerRequestRepository.findByStatus(RequestStatus.PENDING)
                .stream()
                .map(organizerRequestMapper::toResponse)
                .toList();
    }

    public List<OrganizerRequestResponse> getAllRequests(){
        return organizerRequestRepository.findAll()
                .stream()
                .map(organizerRequestMapper::toResponse)
                .toList();
    }

    public OrganizerRequestResponse approveRequest(Long requestId){
        OrganizerRequest request = organizerRequestRepository.findById(requestId)
                .orElseThrow(()-> new BusinessException("Request not found", HttpStatus.NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING){
            throw new BusinessException("Request is already resolved", HttpStatus.BAD_REQUEST);
        }

        request.setStatus(RequestStatus.APPROVED);
        request.setRequestedAt(LocalDateTime.now());
        organizerRequestRepository.save(request);

        User user = request.getUser();
        user.setRole(Role.ORGANIZER);
        userRepository.save(user);

        return organizerRequestMapper.toResponse(request);
    }


    public OrganizerRequestResponse rejectRequest(Long requestId, String rejectionReason){
        OrganizerRequest request = organizerRequestRepository.findById(requestId)
                .orElseThrow(()-> new BusinessException("Request not found", HttpStatus.NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING){
            throw new BusinessException("Request is already resolved", HttpStatus.BAD_REQUEST);
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(rejectionReason);
        request.setResolvedAt(LocalDateTime.now());
        organizerRequestRepository.save(request);

        return organizerRequestMapper.toResponse(request);
    }
}
