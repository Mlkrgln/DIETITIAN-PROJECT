package com.diyetisyen.appointment.entity;

import com.diyetisyen.customer.entity.Customer;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private String cancelReason;

    // 🔹 Müşteri ilişkisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    protected Appointment() {}

    public Appointment(LocalDateTime appointmentDate, Customer customer) {
        this.appointmentDate = appointmentDate;
        this.customer = customer;
        this.status = AppointmentStatus.CREATED;
    }

    // Factory method
    public static Appointment create(LocalDateTime appointmentDate, Customer customer) {
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Geçmiş tarihli randevu olamaz");
        }
        return new Appointment(appointmentDate, customer);
    }

    // cancel / complete metodları aynen
    public void cancel() { cancel(null); }
    public void cancel(String reason) {
        if (status == AppointmentStatus.COMPLETED) throw new IllegalStateException("Tamamlanan randevu iptal edilemez");
        if (appointmentDate.isBefore(LocalDateTime.now())) throw new IllegalStateException("Geçmiş randevu iptal edilemez");
        this.status = AppointmentStatus.CANCELLED;
        this.cancelReason = reason;
    }
    public void complete() { if(status == AppointmentStatus.CANCELLED) throw new IllegalStateException("İptal edilen randevu tamamlanamaz"); status = AppointmentStatus.COMPLETED; }

    public AppointmentStatus getStatus() { return status; }
    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
}