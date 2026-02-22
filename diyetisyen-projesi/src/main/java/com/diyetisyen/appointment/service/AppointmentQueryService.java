package com.diyetisyen.appointment.service;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.repository.AppointmentRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentQueryService {

    private final AppointmentRepository repository;

    public AppointmentQueryService(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "appointments", key = "#customerId")
    public List<Appointment> getCustomerAppointments(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @CacheEvict(value = "appointments", key = "#customerId")
    public void clearCustomerAppointmentsCache(Long customerId) {}
}