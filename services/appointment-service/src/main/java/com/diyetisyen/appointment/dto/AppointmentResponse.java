package com.diyetisyen.appointment.dto;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.entity.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(Long id, Long customerUserId, LocalDateTime appointmentDate,
                                  AppointmentStatus status, String cancellationReason,
                                  LocalDateTime createdAt) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(appointment.getId(), appointment.getAuthUserId(),
                appointment.getAppointmentDate(), appointment.getStatus(),
                appointment.getCancellationReason(), appointment.getCreatedAt());
    }
}
