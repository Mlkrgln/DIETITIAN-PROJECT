package com.diyetisyen.dietlist.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diet_lists")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;  // Hangi müşteriye ait

    private String description; // Diyet açıklaması

    private LocalDateTime createdDate;
}
