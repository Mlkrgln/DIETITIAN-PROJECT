package com.diyetisyen.appointment.service.impl;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.mapper.AppointmentMapper;
import com.diyetisyen.appointment.model.request.AppointmentCreateRequest;
import com.diyetisyen.appointment.model.response.AppointmentResponse;
import com.diyetisyen.appointment.repository.AppointmentRepository;
import com.diyetisyen.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;

    public AppointmentServiceImpl(AppointmentRepository repository,
                                  AppointmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AppointmentResponse create(AppointmentCreateRequest request) {
        Appointment appointment = new Appointment(request.getAppointmentmentDate());
        repository.save(appointment);
        return mapper.toResponse(appointment);
    }

    @Override
    public void cancel(Long id) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
        appointment.cancel();
    }

    @Override
    public void cancel(Long id, String reason) {

    }

    @Override
    public void complete(Long id) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
        appointment.complete();
    }

    @Override
    public AppointmentResponse getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
    }

}
