package com.diyetisyen.appointment.service;

import com.diyetisyen.appointment.dto.AppointmentCreateRequest;
import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.entity.AppointmentOutboxMessage;
import com.diyetisyen.appointment.repository.AppointmentOutboxRepository;
import com.diyetisyen.appointment.repository.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AppointmentService {
    private final AppointmentRepository repository;
    private final AppointmentOutboxRepository outboxRepository;

    public AppointmentService(AppointmentRepository repository, AppointmentOutboxRepository outboxRepository) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
    }

    public Appointment create(Long customerUserId, String email, LocalDateTime appointmentDate) {
        if (customerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token does not include a user id");
        }
        Appointment appointment = repository.save(new Appointment(customerUserId, appointmentDate, email));
        publish(appointment, "created");
        return appointment;
    }

    public Appointment cancelForCustomer(Long id, Long customerUserId, String reason) {
        Appointment appointment = find(id);
        if (!appointment.getAuthUserId().equals(customerUserId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found");
        }
        appointment.cancel(reason);
        publish(appointment, "cancelled");
        return appointment;
    }

    public Appointment cancelByStaff(Long id, String reason) {
        Appointment appointment = find(id);
        appointment.cancel(reason);
        publish(appointment, "cancelled");
        return appointment;
    }

    public Appointment complete(Long id) {
        Appointment appointment = find(id);
        appointment.complete();
        publish(appointment, "completed");
        return appointment;
    }

    @Transactional(readOnly = true)
    public List<Appointment> getMine(Long customerUserId) {
        return repository.findByAuthUserIdOrderByAppointmentDateDesc(customerUserId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Appointment getById(Long id) {
        return find(id);
    }

    private Appointment find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
    }

    private void publish(Appointment appointment, String type) {
        outboxRepository.save(new AppointmentOutboxMessage(
                UUID.randomUUID().toString(), appointment.getId(), appointment.getAuthUserId(),
                appointment.getRecipientEmail(), type, appointment.getAppointmentDate(), LocalDateTime.now()));
    }
}
