package com.diyetisyen.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_records")
@Getter
@NoArgsConstructor
public class NotificationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String eventId;

    @Column(nullable = false)
    private Long appointmentId;

    @Column(nullable = false, length = 30)
    private String eventType;

    @Column(length = 320)
    private String recipientEmail;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public NotificationRecord(String eventId, Long appointmentId, String eventType,
                              String recipientEmail, LocalDateTime appointmentDate) {
        this.eventId = eventId;
        this.appointmentId = appointmentId;
        this.eventType = eventType;
        this.recipientEmail = recipientEmail;
        this.appointmentDate = appointmentDate;
        this.status = NotificationStatus.WAITING_CONFIGURATION;
        this.createdAt = LocalDateTime.now();
    }

    public void mark(NotificationStatus status) {
        this.status = status;
    }
}
