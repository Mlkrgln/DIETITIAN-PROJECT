package com.diyetisyen.customer.dto;

import com.diyetisyen.customer.entity.CustomerProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerProfileResponse(
        Long id,
        Long authUserId,
        String phone,
        LocalDate birthDate,
        String gender,
        LocalDateTime createdAt) {

    public static CustomerProfileResponse from(CustomerProfile profile) {
        return new CustomerProfileResponse(
                profile.getId(), profile.getAuthUserId(), profile.getPhone(),
                profile.getBirthDate(), profile.getGender(), profile.getCreatedAt());
    }
}
