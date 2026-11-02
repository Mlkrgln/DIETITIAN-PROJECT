package com.diyetisyen.appointment.dto;

import jakarta.validation.constraints.Size;

public record AppointmentCancelRequest(@Size(max = 500) String reason) {
}
