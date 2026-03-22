package com.cognizant.billing;

import com.cognizant.billing.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestDb {
    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            JdbcTemplate jdbc = ctx.getBean(JdbcTemplate.class);
            Integer one = jdbc.queryForObject("SELECT 1", Integer.class);
            System.out.println("DB OK! Result = " + one);
        }
    }
}