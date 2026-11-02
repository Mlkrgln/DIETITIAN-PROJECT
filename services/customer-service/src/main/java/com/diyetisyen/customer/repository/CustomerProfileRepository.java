package com.diyetisyen.customer.repository;

import com.diyetisyen.customer.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    Optional<CustomerProfile> findByAuthUserId(Long authUserId);
}
