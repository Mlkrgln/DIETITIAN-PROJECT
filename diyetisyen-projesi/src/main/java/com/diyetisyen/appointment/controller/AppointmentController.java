package com.diyetisyen.appointment.controller;

import com.diyetisyen.appointment.model.request.AppointmentCreateRequest;
import com.diyetisyen.appointment.model.response.AppointmentResponse;
import com.diyetisyen.appointment.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@RequestBody AppointmentCreateRequest request) {
        return appointmentService.create(request);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        appointmentService.cancel(id);
    }

    @PostMapping("/{id}/complete")
    public void complete(@PathVariable Long id) {
        appointmentService.complete(id);
    }

    @GetMapping("/{id}")
    public AppointmentResponse getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }
}
