package com.diyetisyen.appointment.model.response;

import com.diyetisyen.appointment.entity.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentResponse {
    private Long id;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
}
