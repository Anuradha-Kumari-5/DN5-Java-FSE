-- Schema for the "Control Structures" PL/SQL exercises.
-- Run this first, before sample_data.sql or any of the scenario scripts.

CREATE TABLE customers (
    customer_id   NUMBER PRIMARY KEY,
    name          VARCHAR2(100) NOT NULL,
    age           NUMBER(3),
    balance       NUMBER(12,2) DEFAULT 0,
    -- Oracle table columns cannot be a native BOOLEAN type (that's only
    -- true from Oracle 23c onward, and even then it's uncommon practice).
    -- 'Y'/'N' is the standard Oracle convention for a flag column.
    is_vip        VARCHAR2(1) DEFAULT 'N' CHECK (is_vip IN ('Y', 'N'))
);

CREATE TABLE loans (
    loan_id        NUMBER PRIMARY KEY,
    customer_id    NUMBER NOT NULL REFERENCES customers(customer_id),
    principal      NUMBER(12,2),
    interest_rate  NUMBER(5,2),   -- stored as a plain percentage, e.g. 8.50 means 8.50%
    due_date       DATE
);
