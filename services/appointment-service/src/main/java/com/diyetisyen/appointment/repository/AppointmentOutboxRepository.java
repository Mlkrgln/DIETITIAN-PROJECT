package com.diyetisyen.appointment.repository;

import com.diyetisyen.appointment.entity.AppointmentOutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentOutboxRepository extends JpaRepository<AppointmentOutboxMessage, Long> {
    List<AppointmentOutboxMessage> findTop50ByPublishedAtIsNullOrderByCreatedAtAsc();
}
