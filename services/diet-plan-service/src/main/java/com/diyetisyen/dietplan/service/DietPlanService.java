package com.diyetisyen.dietplan.service;

import com.diyetisyen.dietplan.dto.DietMealRequest;
import com.diyetisyen.dietplan.dto.DietPlanCreateRequest;
import com.diyetisyen.dietplan.dto.DietPlanResponse;
import com.diyetisyen.dietplan.entity.DietMeal;
import com.diyetisyen.dietplan.entity.DietPlan;
import com.diyetisyen.dietplan.repository.DietPlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class DietPlanService {
    private final DietPlanRepository repository;

    public DietPlanService(DietPlanRepository repository) {
        this.repository = repository;
    }

    public DietPlanResponse create(DietPlanCreateRequest request) {
        if (request.startDate().isAfter(request.endDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate must be on or before endDate");
        }
        DietPlan plan = new DietPlan(request.customerUserId(), request.title(),
                request.startDate(), request.endDate());
        for (DietMealRequest meal : request.meals()) {
            if (meal.mealDate().isBefore(request.startDate()) || meal.mealDate().isAfter(request.endDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meal date must be within the plan dates");
            }
            plan.addMeal(new DietMeal(meal.mealDate(), meal.mealType(), meal.description(), meal.calories()));
        }
        return DietPlanResponse.from(repository.save(plan));
    }

    @Transactional(readOnly = true)
    public List<DietPlanResponse> getMine(Long customerUserId) {
        return repository.findByCustomerUserIdOrderByStartDateDesc(customerUserId)
                .stream().map(DietPlanResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public DietPlanResponse getMineById(Long id, Long customerUserId) {
        DietPlan plan = repository.findByIdAndCustomerUserId(id, customerUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diet plan not found"));
        return DietPlanResponse.from(plan);
    }

    @Transactional(readOnly = true)
    public List<DietPlanResponse> getForCustomer(Long customerUserId) {
        return repository.findByCustomerUserIdOrderByStartDateDesc(customerUserId)
                .stream().map(DietPlanResponse::from).toList();
    }
}
