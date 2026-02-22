package com.diyetisyen.appointment.controller;

import com.diyetisyen.appointment.model.request.AppointmentCreateRequest;
import com.diyetisyen.appointment.model.response.AppointmentResponse;
import com.diyetisyen.appointment.service.AppointmentService;
import com.diyetisyen.customer.entity.Customer;
import com.diyetisyen.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final CustomerRepository customerRepository;

    public AppointmentController(AppointmentService appointmentService, CustomerRepository customerRepository) {
        this.appointmentService = appointmentService;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public AppointmentResponse create(@RequestBody AppointmentCreateRequest request,
                                      @RequestParam Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer bulunamadı"));

        return appointmentService.create(request, customer);
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
