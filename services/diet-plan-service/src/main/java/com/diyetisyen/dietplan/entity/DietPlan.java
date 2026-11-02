package com.diyetisyen.dietplan.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diet_plans")
@Getter
@NoArgsConstructor
public class DietPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerUserId;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("mealDate ASC, id ASC")
    private List<DietMeal> meals = new ArrayList<>();

    public DietPlan(Long customerUserId, String title, LocalDate startDate, LocalDate endDate) {
        this.customerUserId = customerUserId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = LocalDateTime.now();
    }

    public void addMeal(DietMeal meal) {
        meals.add(meal);
        meal.setPlan(this);
    }
}
