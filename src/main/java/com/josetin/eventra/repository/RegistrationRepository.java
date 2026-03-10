package com.josetin.eventra.repository;

import com.josetin.eventra.entity.Event;
import com.josetin.eventra.entity.Registration;
import com.josetin.eventra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserAndEvent (User user, Event event);
}
