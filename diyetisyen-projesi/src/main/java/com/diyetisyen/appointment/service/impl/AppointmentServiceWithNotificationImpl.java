package com.diyetisyen.appointment.service.impl;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.model.request.AppointmentCreateRequest;
import com.diyetisyen.appointment.model.response.AppointmentResponse;
import com.diyetisyen.appointment.repository.AppointmentRepository;
import com.diyetisyen.appointment.service.AppointmentService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary // Hangisi çalışsın diye Spring'e söylüyoruz  //@Qualifier
public class AppointmentServiceWithNotificationImpl implements AppointmentService {


    private final AppointmentRepository repository;

    public AppointmentServiceWithNotificationImpl(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public AppointmentResponse create(AppointmentCreateRequest request) {
        return null;
    }

    @Override
    public void cancel(Long id) {
        Appointment appointment = repository.findById(id)
                .orElseThrow();

        appointment.cancel();

        sendNotification();
    }

    @Override
    public void cancel(Long id, String reason) {
        
    }

    @Override
    public void complete(Long id) {

    }

    @Override
    public AppointmentResponse getById(Long id) {
        return null;
    }
    private void sendNotification() {
        System.out.println("Mail + Kafka gönderildi");
    }
}
