package com.diyetisyen.dietplan.controller;

import com.diyetisyen.dietplan.dto.DietPlanCreateRequest;
import com.diyetisyen.dietplan.dto.DietPlanResponse;
import com.diyetisyen.dietplan.service.DietPlanService;
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
@RequestMapping("/api/diet-plans")
public class DietPlanController {
    private final DietPlanService service;

    public DietPlanController(DietPlanService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public DietPlanResponse create(@Valid @RequestBody DietPlanCreateRequest request) {
        return service.create(request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<DietPlanResponse> mine(@AuthenticationPrincipal Jwt jwt) {
        return service.getMine(requiredUserId(jwt));
    }

    @GetMapping("/me/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public DietPlanResponse mineById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return service.getMineById(id, requiredUserId(jwt));
    }

    @GetMapping("/for-customer/{customerUserId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIETITIAN')")
    public List<DietPlanResponse> forCustomer(@PathVariable Long customerUserId) {
        return service.getForCustomer(customerUserId);
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
