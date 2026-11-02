package com.diyetisyen.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentCreateRequest(@NotNull @Future LocalDateTime appointmentDate) {
}
