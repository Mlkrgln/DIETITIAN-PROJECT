package com.diyetisyen.appointment.event;
public class AppointmentCompletedEvent {
    private final Long appointmentId;
    public AppointmentCompletedEvent(Long appointmentId) { this.appointmentId = appointmentId; }
    public Long getAppointmentId() { return appointmentId; }
}
