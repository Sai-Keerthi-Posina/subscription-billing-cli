package com.cognizant.billing.dao;

import com.cognizant.billing.model.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanDao {
    Plan save(Plan plan);                 // Insert and return with generated id
    Optional<Plan> findById(Long id);
    Optional<Plan> findByName(String name);
    List<Plan> findAll();
}