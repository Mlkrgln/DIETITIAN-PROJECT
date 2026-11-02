package com.diyetisyen.dietplan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "diet_meals")
@Getter
@NoArgsConstructor
public class DietMeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private DietPlan plan;

    @Column(nullable = false)
    private LocalDate mealDate;

    @Column(nullable = false, length = 40)
    private String mealType;

    @Column(nullable = false, length = 1000)
    private String description;

    private Integer calories;

    public DietMeal(LocalDate mealDate, String mealType, String description, Integer calories) {
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.description = description;
        this.calories = calories;
    }

    void setPlan(DietPlan plan) {
        this.plan = plan;
    }
}
