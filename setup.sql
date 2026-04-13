-- ═══════════════════════════════════════════════════════════════════════
-- Employee Management System — Database Setup Script
-- Run this once against your MySQL server before starting Tomcat.
--
-- Usage (MySQL CLI):
--   mysql -u root -p < setup.sql
-- or paste into MySQL Workbench / DBeaver
-- ═══════════════════════════════════════════════════════════════════════

-- 1. Create and select database
CREATE DATABASE IF NOT EXISTS empmanagement
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE empmanagement;

-- ───────────────────────────────────────────────────────────────────────
-- 2. Users table (admin login accounts)
-- ───────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id         INT          NOT NULL AUTO_INCREMENT,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Default admin user  (username: admin@ems.com | password: admin123)
INSERT IGNORE INTO users (username, password)
VALUES ('admin@ems.com', 'admin123');

-- ───────────────────────────────────────────────────────────────────────
-- 3. Employees table
-- ───────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS employees (
    id         INT          NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(20)  DEFAULT NULL,
    department VARCHAR(50)  DEFAULT NULL,
    role       VARCHAR(100) DEFAULT NULL,
    country    VARCHAR(50)  DEFAULT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ───────────────────────────────────────────────────────────────────────
-- 4. Sample employee data (optional — delete if not needed)
-- ───────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO employees (name, email, phone, department, role, country) VALUES
('Ravi Kumar',     'ravi.kumar@ems.com',     '+91 98765 43210', 'Engineering',     'Software Engineer',       'India'),
('Priya Sharma',   'priya.sharma@ems.com',   '+91 87654 32109', 'Human Resources', 'HR Manager',              'India'),
('Arjun Mehta',    'arjun.mehta@ems.com',    '+91 76543 21098', 'Finance',         'Financial Analyst',       'India'),
('Sneha Patil',    'sneha.patil@ems.com',     NULL,             'Marketing',       'Marketing Lead',          'India'),
('Vikram Singh',   'vikram.singh@ems.com',    NULL,             'Operations',      'Operations Manager',      'India'),
('Ananya Nair',    'ananya.nair@ems.com',    '+91 65432 10987', 'Engineering',     'Full Stack Developer',    'India'),
('Rahul Gupta',    'rahul.gupta@ems.com',    '+91 54321 09876', 'IT Support',      'Systems Administrator',   'India'),
('Meera Iyer',     'meera.iyer@ems.com',      NULL,             'Design',          'UI/UX Designer',          'India'),
('Deepak Joshi',   'deepak.joshi@ems.com',   '+91 43210 98765', 'Sales',           'Sales Executive',         'India'),
('Kavya Reddy',    'kavya.reddy@ems.com',    '+91 32109 87654', 'Engineering',     'Backend Developer',       'India'),
('Arun Thomas',    'arun.thomas@ems.com',    '+91 21098 76543', 'Management',      'Project Manager',         'India'),
('Lakshmi Rao',    'lakshmi.rao@ems.com',     NULL,             'Legal',           'Legal Counsel',           'India');

-- ═══════════════════════════════════════════════════════════════════════
-- Setup complete. Start Tomcat and navigate to:
-- http://localhost:8080/Mini%20Project/login.jsp
-- Login: admin@ems.com  |  Password: admin123
-- ═══════════════════════════════════════════════════════════════════════
