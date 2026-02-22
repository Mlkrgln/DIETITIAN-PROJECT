package com.diyetisyen.appointment.event;

public class AppointmentCreatedEvent {
    private final Long appointmentId;
    public AppointmentCreatedEvent(Long appointmentId) { this.appointmentId = appointmentId; }
    public Long getAppointmentId() { return appointmentId; }
}