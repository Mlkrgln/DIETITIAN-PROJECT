package com.diyetisyen.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_profiles")
@Getter
@Setter
@NoArgsConstructor
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long authUserId;

    @Column(length = 30)
    private String phone;

    private LocalDate birthDate;

    @Column(length = 50)
    private String gender;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public CustomerProfile(Long authUserId, String phone, LocalDate birthDate, String gender) {
        this.authUserId = authUserId;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.createdAt = LocalDateTime.now();
    }
}
