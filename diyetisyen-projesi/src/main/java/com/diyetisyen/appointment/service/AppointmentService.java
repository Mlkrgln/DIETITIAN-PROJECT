package com.diyetisyen.appointment.service;

import com.diyetisyen.appointment.model.request.AppointmentCreateRequest;
import com.diyetisyen.appointment.model.response.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse create(AppointmentCreateRequest request);

    void cancel(Long id);

    void complete(Long id);

    AppointmentResponse getById(Long id);
}
