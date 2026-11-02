package com.diyetisyen.dietplan.service;

import com.diyetisyen.dietplan.dto.DietMealRequest;
import com.diyetisyen.dietplan.dto.DietPlanCreateRequest;
import com.diyetisyen.dietplan.dto.DietPlanResponse;
import com.diyetisyen.dietplan.repository.DietPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DietPlanServiceTest {
    @Mock DietPlanRepository repository;
    @InjectMocks DietPlanService service;

    @Test
    void persistsPlanAndItsMeals() {
        LocalDate start = LocalDate.of(2026, 11, 2);
        LocalDate end = start.plusDays(6);
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));

        DietPlanResponse response = service.create(new DietPlanCreateRequest(7L, "Haftalık plan", start, end,
                List.of(new DietMealRequest(start, "Kahvaltı", "Yulaf ve yoğurt", 350))));

        assertEquals(7L, response.customerUserId());
        assertEquals("Yulaf ve yoğurt", response.meals().get(0).description());
        verify(repository).save(any());
    }

    @Test
    void rejectsMealOutsidePlanDates() {
        LocalDate start = LocalDate.of(2026, 11, 2);
        LocalDate end = start.plusDays(2);
        DietPlanCreateRequest request = new DietPlanCreateRequest(7L, "Kısa plan", start, end,
                List.of(new DietMealRequest(end.plusDays(1), "Akşam", "Sebze", null)));

        assertThrows(ResponseStatusException.class, () -> service.create(request));
    }
}
