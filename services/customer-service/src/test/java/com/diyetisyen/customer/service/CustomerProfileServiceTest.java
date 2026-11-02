package com.diyetisyen.customer.service;

import com.diyetisyen.customer.dto.CustomerProfileRequest;
import com.diyetisyen.customer.entity.CustomerProfile;
import com.diyetisyen.customer.repository.CustomerProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerProfileServiceTest {
    @Mock CustomerProfileRepository repository;
    @InjectMocks CustomerProfileService service;

    @Test
    void createsProfileOwnedByAuthenticatedUser() {
        when(repository.findByAuthUserId(17L)).thenReturn(Optional.empty());
        when(repository.save(any(CustomerProfile.class))).thenAnswer(call -> call.getArgument(0));

        CustomerProfile profile = service.create(17L,
                new CustomerProfileRequest("5550000000", LocalDate.of(1995, 4, 2), "Belirtmek istemiyorum"));

        assertEquals(17L, profile.getAuthUserId());
        assertEquals("5550000000", profile.getPhone());
    }

    @Test
    void rejectsSecondProfileForSameAuthUser() {
        when(repository.findByAuthUserId(17L)).thenReturn(Optional.of(new CustomerProfile()));

        assertThrows(ResponseStatusException.class,
                () -> service.create(17L, new CustomerProfileRequest(null, null, null)));
        verify(repository, never()).save(any());
    }
}
