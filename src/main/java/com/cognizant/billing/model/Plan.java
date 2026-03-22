package com.cognizant.billing.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Plan {
    private Long id;
    private String name;
    private BigDecimal price;
    private BillingCycle cycle;

    public Plan() {}

    public Plan(Long id, String name, BigDecimal price, BillingCycle cycle) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cycle = cycle;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BillingCycle getCycle() { return cycle; }
    public void setCycle(BillingCycle cycle) { this.cycle = cycle; }

    @Override
    public String toString() {
        return "Plan{id=" + id + ", name='" + name + "', price=" + price + ", cycle=" + cycle + "}";
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plan)) return false;
        Plan plan = (Plan) o;
        return Objects.equals(id, plan.id);
    }

    @Override public int hashCode() {
        return Objects.hash(id);
    }
}