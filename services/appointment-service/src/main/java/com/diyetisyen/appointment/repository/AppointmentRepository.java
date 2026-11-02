package com.diyetisyen.appointment.repository;

import com.diyetisyen.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByAuthUserIdOrderByAppointmentDateDesc(Long authUserId);
}
