package com.diyetisyen.appointment.repository;

import com.diyetisyen.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // 🔹 Müşteriye ait randevuları çekmek için metod
    List<Appointment> findByCustomerId(Long customerId);
}
