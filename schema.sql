CREATE DATABASE IF NOT EXISTS billingdb;
USE billingdb;

CREATE TABLE IF NOT EXISTS plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    price DECIMAL(12,2) NOT NULL,
    cycle ENUM('MONTHLY','YEARLY') NOT NULL
);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id VARCHAR(50) NOT NULL,
    plan_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    status ENUM('ACTIVE','CANCELED') NOT NULL,
    CONSTRAINT fk_sub_plan FOREIGN KEY (plan_id) REFERENCES plans(id)
);

CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscription_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    due_date DATE NOT NULL,
    status ENUM('OPEN','PAID','VOID') NOT NULL,
    CONSTRAINT fk_inv_sub FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscription_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    status ENUM('SUCCESS','FAILED') NOT NULL,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pay_sub FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);