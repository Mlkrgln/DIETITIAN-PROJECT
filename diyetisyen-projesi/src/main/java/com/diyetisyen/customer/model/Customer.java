package com.diyetisyen.customer.model;

import jakarta.persistence.*; // JPA anotasyonları için gerekli
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    private Long id;
    private String name;
    private String email;
    private String phone;
}