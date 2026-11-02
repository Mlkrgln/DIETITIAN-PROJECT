package com.diyetisyen.dietplan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DietMealRequest(
        @NotNull LocalDate mealDate,
        @NotBlank @Size(max = 40) String mealType,
        @NotBlank @Size(max = 1000) String description,
        @PositiveOrZero Integer calories) {
}
