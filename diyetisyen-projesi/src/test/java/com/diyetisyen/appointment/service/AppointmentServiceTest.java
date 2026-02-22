package com.diyetisyen.appointment.service;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.appointment.entity.AppointmentStatus;
import com.diyetisyen.appointment.mapper.AppointmentMapper;
import com.diyetisyen.appointment.repository.AppointmentRepository;
import com.diyetisyen.appointment.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private AppointmentRepository repository;

    @Mock
    private AppointmentMapper mapper;

    @InjectMocks
    private AppointmentServiceImpl service;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void shouldCancelAppointment() {
        Appointment appointment = Appointment.create(LocalDateTime.now().plusDays(1), null);

        when(repository.findById(1L)).thenReturn(Optional.of(appointment));

        service.cancel(1L);

        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
    }


}
