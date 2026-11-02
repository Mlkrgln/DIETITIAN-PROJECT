package com.diyetisyen.appointment.entity;

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
@Table(name = "appointments")
@Getter
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authUserId;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Column(length = 320)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(length = 500)
    private String cancellationReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Appointment(Long authUserId, LocalDateTime appointmentDate, String recipientEmail) {
        this.authUserId = authUserId;
        this.appointmentDate = appointmentDate;
        this.recipientEmail = recipientEmail;
        this.status = AppointmentStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        if (status != AppointmentStatus.CREATED) {
            throw new IllegalStateException("Only active appointments can be cancelled");
        }
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Past appointments cannot be cancelled");
        }
        status = AppointmentStatus.CANCELLED;
        cancellationReason = reason;
    }

    public void complete() {
        if (status != AppointmentStatus.CREATED) {
            throw new IllegalStateException("Only active appointments can be completed");
        }
        if (appointmentDate.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Future appointments cannot be completed");
        }
        status = AppointmentStatus.COMPLETED;
    }
}
