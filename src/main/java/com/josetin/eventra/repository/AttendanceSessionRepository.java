package com.josetin.eventra.repository;

import com.josetin.eventra.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {

    Optional<AttendanceSession> findByToken(String token);

    Optional<AttendanceSession> findByEventId(Long eventId);
}
