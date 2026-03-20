package com.josetin.eventra.repository;

import com.josetin.eventra.entity.OrganizerRequest;
import com.josetin.eventra.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizerRequestRepository extends JpaRepository<OrganizerRequest, Long> {

    List<OrganizerRequest> findByStatus (RequestStatus status);
    
    Optional<OrganizerRequest> findTopByUserIdOrderByRequestedAtDesc(Long userId);

    boolean existsByUserIdAndStatus(Long userId, RequestStatus status);

    Long countByStatus(RequestStatus status);
}
