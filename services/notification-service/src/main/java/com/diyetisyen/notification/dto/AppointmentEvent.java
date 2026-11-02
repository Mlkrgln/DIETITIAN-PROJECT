package com.diyetisyen.notification.dto;

import java.time.LocalDateTime;

public record AppointmentEvent(String eventId, Long appointmentId, Long customerUserId,
                              String recipientEmail, String type,
                              LocalDateTime appointmentDate, LocalDateTime occurredAt) {
}
