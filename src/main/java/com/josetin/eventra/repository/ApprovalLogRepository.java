package com.josetin.eventra.repository;

import com.josetin.eventra.entity.ApprovalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApprovalLogRepository extends JpaRepository<ApprovalLog, Long> {

    Optional<ApprovalLog> findByEventId(Long eventId);
}
