package com.diyetisyen.appointment.service;

import com.diyetisyen.appointment.model.request.AppointmentCreateRequest;
import com.diyetisyen.appointment.model.response.AppointmentResponse;
import com.diyetisyen.customer.entity.Customer;

public interface AppointmentService {

    AppointmentResponse create(AppointmentCreateRequest request, Customer customer);

    void cancel(Long id);
    
    void cancel(Long id, String reason);

    void complete(Long id);

    AppointmentResponse getById(Long id);
}

