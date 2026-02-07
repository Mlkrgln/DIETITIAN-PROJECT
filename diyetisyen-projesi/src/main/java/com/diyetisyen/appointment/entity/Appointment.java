package com.diyetisyen.appointment.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime appointmentDate;

    @Getter
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    protected Appointment() {
    }

    public Appointment(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
        this.status = AppointmentStatus.CREATED;
    }

    public void cancel() {
        if (status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Tamamlanan randevu iptal edilemez");
        }
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Geçmiş randevu iptal edilemez");
        }
        this.status = AppointmentStatus.CANCELLED;
    }

    public void complete() {
        if (status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("İptal edilen randevu tamamlanamaz");
        }
        this.status = AppointmentStatus.COMPLETED;
    }

}
