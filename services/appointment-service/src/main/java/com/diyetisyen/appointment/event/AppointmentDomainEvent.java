package com.diyetisyen.appointment.event;

import java.time.LocalDateTime;

public record AppointmentDomainEvent(String eventId, Long appointmentId, Long customerUserId, String recipientEmail,
                                     String type, LocalDateTime appointmentDate,
                                     LocalDateTime occurredAt) {
}
