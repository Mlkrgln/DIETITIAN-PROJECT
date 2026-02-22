package com.diyetisyen.appointment.service.impl;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.event.AppointmentCancelledEvent;
import com.diyetisyen.appointment.event.AppointmentCreatedEvent;
import com.diyetisyen.appointment.event.AppointmentCompletedEvent;
import com.diyetisyen.appointment.mapper.AppointmentMapper;
import com.diyetisyen.appointment.model.request.AppointmentCreateRequest;
import com.diyetisyen.appointment.model.response.AppointmentResponse;
import com.diyetisyen.appointment.repository.AppointmentRepository;
import com.diyetisyen.appointment.service.AppointmentService;
import com.diyetisyen.customer.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public AppointmentServiceImpl(AppointmentRepository repository,
                                  AppointmentMapper mapper,
                                  ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    // 🔹 CREATE → Transaction + Event + Customer ilişkisi
    @Override
    @Transactional
    public AppointmentResponse create(AppointmentCreateRequest request, Customer customer) {
        Appointment appointment = Appointment.create(request.getAppointmentmentDate()
                , customer);
        repository.save(appointment);

        eventPublisher.publishEvent(new AppointmentCreatedEvent(appointment.getId()));
        return mapper.toResponse(appointment);
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
        appointment.cancel();

        eventPublisher.publishEvent(new AppointmentCancelledEvent(appointment.getId()));
    }

    @Override
    @Transactional
    public void cancel(Long id, String reason) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
        appointment.cancel(reason);

        eventPublisher.publishEvent(new AppointmentCancelledEvent(appointment.getId()));
    }

    @Override
    @Transactional
    public void complete(Long id) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
        appointment.complete();

        eventPublisher.publishEvent(new AppointmentCompletedEvent(appointment.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
    }
}