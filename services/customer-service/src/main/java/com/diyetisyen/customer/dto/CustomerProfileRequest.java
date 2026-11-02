package com.diyetisyen.customer.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CustomerProfileRequest(
        @Size(max = 30) String phone,
        @Past LocalDate birthDate,
        @Size(max = 50) String gender) {
}
