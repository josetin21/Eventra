package com.josetin.eventra.repository;

import com.josetin.eventra.entity.Event;
import com.josetin.eventra.entity.Registration;
import com.josetin.eventra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserAndEvent (User user, Event event);

    boolean existsByUserIdAndEventId (Long userId, Long eventId);

    int countByEventId(Long eventId);

    List<Registration> findByUserId(Long userId);

    List<Registration> findByEventId(Long eventId);

    boolean existsByUserIdAndEventIdAndQrCode(Long id, Long id1, String qrCode);

}
