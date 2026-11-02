package com.diyetisyen.appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_outbox")
@Getter
@NoArgsConstructor
public class AppointmentOutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String eventId;

    @Column(nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private Long customerUserId;

    @Column(length = 320)
    private String recipientEmail;

    @Column(nullable = false, length = 30)
    private String eventType;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    public AppointmentOutboxMessage(String eventId, Long appointmentId, Long customerUserId,
                                    String recipientEmail, String eventType,
                                    LocalDateTime appointmentDate, LocalDateTime occurredAt) {
        this.eventId = eventId;
        this.appointmentId = appointmentId;
        this.customerUserId = customerUserId;
        this.recipientEmail = recipientEmail;
        this.eventType = eventType;
        this.appointmentDate = appointmentDate;
        this.occurredAt = occurredAt;
        this.createdAt = LocalDateTime.now();
    }

    public void markPublished(LocalDateTime time) {
        this.publishedAt = time;
    }
}
