package com.cognizant.billing.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.cognizant.billing")
@Import(DbConnection.class)
public class AppConfig {
    // Scans your packages and imports DB config.
}