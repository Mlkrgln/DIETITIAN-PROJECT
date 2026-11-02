package com.diyetisyen.dietplan.dto;

import com.diyetisyen.dietplan.entity.DietPlan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DietPlanResponse(Long id, Long customerUserId, String title, LocalDate startDate,
                               LocalDate endDate, LocalDateTime createdAt, List<DietMealResponse> meals) {
    public static DietPlanResponse from(DietPlan plan) {
        List<DietMealResponse> mealResponses = plan.getMeals().stream()
                .map(meal -> new DietMealResponse(meal.getId(), meal.getMealDate(), meal.getMealType(),
                        meal.getDescription(), meal.getCalories()))
                .toList();
        return new DietPlanResponse(plan.getId(), plan.getCustomerUserId(), plan.getTitle(),
                plan.getStartDate(), plan.getEndDate(), plan.getCreatedAt(), mealResponses);
    }

    public record DietMealResponse(Long id, LocalDate mealDate, String mealType,
                                   String description, Integer calories) {
    }
}
