package com.diyetisyen.dietlist.repository;

import com.diyetisyen.dietlist.model.entity.DietList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DietListRepository extends JpaRepository<DietList, Long> {
    List<DietList> findByCustomerId(Long customerId);
}