package com.diyetisyen.customer.service;

import com.diyetisyen.customer.dto.CustomerProfileRequest;
import com.diyetisyen.customer.entity.CustomerProfile;
import com.diyetisyen.customer.repository.CustomerProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CustomerProfileService {

    private final CustomerProfileRepository repository;

    public CustomerProfileService(CustomerProfileRepository repository) {
        this.repository = repository;
    }

    public CustomerProfile create(Long authUserId, CustomerProfileRequest request) {
        if (repository.findByAuthUserId(authUserId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer profile already exists");
        }
        return repository.save(new CustomerProfile(
                authUserId, request.phone(), request.birthDate(), request.gender()));
    }

    @Transactional(readOnly = true)
    public CustomerProfile getByAuthUserId(Long authUserId) {
        return repository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer profile not found"));
    }

    @Transactional(readOnly = true)
    public CustomerProfile getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer profile not found"));
    }

    @Transactional(readOnly = true)
    public List<CustomerProfile> getAll() {
        return repository.findAll();
    }
}
