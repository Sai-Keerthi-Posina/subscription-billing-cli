package com.cognizant.billing;

import com.cognizant.billing.cli.ConsoleRunner;
import com.cognizant.billing.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            ctx.getBean(ConsoleRunner.class).start();
        }
    }
}