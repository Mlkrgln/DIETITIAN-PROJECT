package com.diyetisyen.dietplan.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record DietPlanCreateRequest(
        @NotNull Long customerUserId,
        @NotBlank @Size(max = 120) String title,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotEmpty List<@Valid DietMealRequest> meals) {
}
