-- Schema for the "Stored Procedures" exercises.
-- Run this first, before sample_data.sql or any procedure script.

CREATE TABLE accounts (
    account_id    NUMBER PRIMARY KEY,
    customer_name VARCHAR2(100) NOT NULL,
    account_type  VARCHAR2(20) NOT NULL CHECK (account_type IN ('SAVINGS', 'CURRENT')),
    balance       NUMBER(12,2) DEFAULT 0 NOT NULL
);

CREATE TABLE employees (
    employee_id NUMBER PRIMARY KEY,
    name        VARCHAR2(100) NOT NULL,
    department  VARCHAR2(50) NOT NULL,
    salary      NUMBER(12,2) NOT NULL
);
