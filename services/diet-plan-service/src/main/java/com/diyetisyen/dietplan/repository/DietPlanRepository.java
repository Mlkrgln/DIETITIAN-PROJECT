package com.diyetisyen.dietplan.repository;

import com.diyetisyen.dietplan.entity.DietPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    @EntityGraph(attributePaths = "meals")
    List<DietPlan> findByCustomerUserIdOrderByStartDateDesc(Long customerUserId);

    @EntityGraph(attributePaths = "meals")
    Optional<DietPlan> findByIdAndCustomerUserId(Long id, Long customerUserId);

    @Override
    @EntityGraph(attributePaths = "meals")
    List<DietPlan> findAll();
}
