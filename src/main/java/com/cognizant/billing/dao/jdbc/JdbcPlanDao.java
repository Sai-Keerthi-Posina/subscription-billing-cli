package com.cognizant.billing.dao.jdbc;

import com.cognizant.billing.dao.PlanDao;
import com.cognizant.billing.model.BillingCycle;
import com.cognizant.billing.model.Plan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPlanDao implements PlanDao {

    private final JdbcTemplate jdbc;

    public JdbcPlanDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Plan save(Plan plan) {
        String sql = "INSERT INTO plans(name, price, cycle) VALUES (?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, plan.getName());
            ps.setBigDecimal(2, plan.getPrice());
            ps.setString(3, plan.getCycle().name());
            return ps;
        }, kh);
        Number key = kh.getKey();
        if (key != null) plan.setId(key.longValue());
        return plan;
    }

    @Override
    public Optional<Plan> findById(Long id) {
        String sql = "SELECT id, name, price, cycle FROM plans WHERE id = ?";
        List<Plan> list = jdbc.query(sql, (rs, rowNum) -> {
            Long pid = rs.getLong("id");
            String name = rs.getString("name");
            BigDecimal price = rs.getBigDecimal("price");
            BillingCycle cycle = BillingCycle.valueOf(rs.getString("cycle"));
            return new Plan(pid, name, price, cycle);
        }, id);
        return list.stream().findFirst();
    }

    @Override
    public Optional<Plan> findByName(String name) {
        String sql = "SELECT id, name, price, cycle FROM plans WHERE name = ?";
        List<Plan> list = jdbc.query(sql, (rs, rowNum) -> {
            Long pid = rs.getLong("id");
            String pname = rs.getString("name");
            BigDecimal price = rs.getBigDecimal("price");
            BillingCycle cycle = BillingCycle.valueOf(rs.getString("cycle"));
            return new Plan(pid, pname, price, cycle);
        }, name);
        return list.stream().findFirst();
    }

    @Override
    public List<Plan> findAll() {
        String sql = "SELECT id, name, price, cycle FROM plans ORDER BY id";
        return jdbc.query(sql, (rs, rowNum) ->
                new Plan(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        BillingCycle.valueOf(rs.getString("cycle"))
                )
        );
    }
}