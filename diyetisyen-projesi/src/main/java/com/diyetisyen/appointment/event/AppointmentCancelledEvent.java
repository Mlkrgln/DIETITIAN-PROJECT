package com.diyetisyen.appointment.event;

public class AppointmentCancelledEvent {
    private final Long appointmentId;

    public AppointmentCancelledEvent(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }
}
