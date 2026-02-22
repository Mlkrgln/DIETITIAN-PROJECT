package com.diyetisyen.appointment.mapper;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.model.response.AppointmentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentResponse toResponse(Appointment appointment);
}