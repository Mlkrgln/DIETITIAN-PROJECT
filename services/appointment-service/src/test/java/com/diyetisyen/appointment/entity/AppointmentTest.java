package com.diyetisyen.appointment.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentTest {
    @Test
    void cancellationChangesStateAndCannotBeRepeated() {
        Appointment appointment = new Appointment(10L, LocalDateTime.now().plusDays(1), "user@example.test");

        appointment.cancel("Schedule change");

        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertEquals("Schedule change", appointment.getCancellationReason());
        assertThrows(IllegalStateException.class, () -> appointment.cancel(null));
    }

    @Test
    void futureAppointmentCannotBeCompleted() {
        Appointment appointment = new Appointment(10L, LocalDateTime.now().plusDays(1), "user@example.test");

        assertThrows(IllegalStateException.class, appointment::complete);
        assertEquals(AppointmentStatus.CREATED, appointment.getStatus());
    }
}
