package com.diyetisyen.appointment.model.request;

import java.time.LocalDateTime;

public class AppointmentCreateRequest {
    private LocalDateTime appointmentDate;

    public LocalDateTime getAppointmentmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
