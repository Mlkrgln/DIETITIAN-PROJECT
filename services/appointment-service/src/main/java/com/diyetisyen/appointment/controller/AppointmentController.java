package com.diyetisyen.appointment.controller;

import com.diyetisyen.appointment.dto.AppointmentCancelRequest;
import com.diyetisyen.appointment.dto.AppointmentCreateRequest;
import com.diyetisyen.appointment.dto.AppointmentResponse;
import com.diyetisyen.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public AppointmentResponse create(@AuthenticationPrincipal Jwt jwt,
                                     @Valid @RequestBody AppointmentCreateRequest request) {
        Number id = jwt.getClaim("userId");
        return AppointmentResponse.from(service.create(id == null ? null : id.longValue(),
                jwt.getClaimAsString("email"), request.appointmentDate()));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<AppointmentResponse> mine(@AuthenticationPrincipal Jwt jwt) {
        return service.getMine(requiredUserId(jwt)).stream().map(AppointmentResponse::from).toList();
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public AppointmentResponse cancelMine(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id,
                                          @Valid @RequestBody(required = false) AppointmentCancelRequest request) {
        return AppointmentResponse.from(service.cancelForCustomer(id, requiredUserId(jwt),
                request == null ? null : request.reason()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public List<AppointmentResponse> all() {
        return service.getAll().stream().map(AppointmentResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public AppointmentResponse getById(@PathVariable Long id) {
        return AppointmentResponse.from(service.getById(id));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public AppointmentResponse complete(@PathVariable Long id) {
        return AppointmentResponse.from(service.complete(id));
    }

    @PostMapping("/admin/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public AppointmentResponse cancelByStaff(@PathVariable Long id,
                                             @Valid @RequestBody(required = false) AppointmentCancelRequest request) {
        return AppointmentResponse.from(service.cancelByStaff(id, request == null ? null : request.reason()));
    }

    private Long requiredUserId(Jwt jwt) {
        Number id = jwt.getClaim("userId");
        if (id == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token does not include a user id");
        }
        return id.longValue();
    }
}
