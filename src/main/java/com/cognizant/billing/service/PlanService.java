package com.cognizant.billing.service;

import com.cognizant.billing.dao.PlanDao;
import com.cognizant.billing.model.BillingCycle;
import com.cognizant.billing.model.Plan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlanService {

    private final PlanDao planDao;

    public PlanService(PlanDao planDao) {
        this.planDao = planDao;
    }

    public Plan addPlan(String name, BigDecimal price, BillingCycle cycle) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (price == null || price.signum() <= 0) throw new IllegalArgumentException("price must be > 0");
        if (cycle == null) throw new IllegalArgumentException("cycle required");
        planDao.findByName(name).ifPresent(p -> {
            throw new IllegalArgumentException("Plan name already exists");
        });
        return planDao.save(new Plan(null, name, price, cycle));
    }

    public List<Plan> list() {
        return planDao.findAll();
    }
}