package com.diyetisyen.customer.controller;

import com.diyetisyen.customer.dto.CustomerProfileRequest;
import com.diyetisyen.customer.dto.CustomerProfileResponse;
import com.diyetisyen.customer.service.CustomerProfileService;
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
@RequestMapping("/api/customers")
public class CustomerProfileController {

    private final CustomerProfileService service;

    public CustomerProfileController(CustomerProfileService service) {
        this.service = service;
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public CustomerProfileResponse createMine(@AuthenticationPrincipal Jwt jwt,
                                              @Valid @RequestBody CustomerProfileRequest request) {
        return CustomerProfileResponse.from(service.create(requiredUserId(jwt), request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CustomerProfileResponse getMine(@AuthenticationPrincipal Jwt jwt) {
        return CustomerProfileResponse.from(service.getByAuthUserId(requiredUserId(jwt)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public List<CustomerProfileResponse> getAll() {
        return service.getAll().stream().map(CustomerProfileResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public CustomerProfileResponse getById(@PathVariable Long id) {
        return CustomerProfileResponse.from(service.getById(id));
    }

    private Long requiredUserId(Jwt jwt) {
        Number userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token does not include a user id");
        }
        return userIdClaim.longValue();
    }
}
