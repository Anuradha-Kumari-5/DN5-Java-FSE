-- Run these manually in MySQL Workbench or the mysql client BEFORE
-- starting the application. spring.jpa.hibernate.ddl-auto=validate
-- means Hibernate will only *check* that this table matches the
-- Country entity -- it will NOT create it for you.

-- 1. Create the schema (only needed once)
-- mysql -u root -p
-- mysql> create schema ormlearn;

-- 2. Switch to it, then run the rest of this file
USE ormlearn;

CREATE TABLE country (
    co_code VARCHAR(2) PRIMARY KEY,
    co_name VARCHAR(50)
);

INSERT INTO country VALUES ('IN', 'India');
INSERT INTO country VALUES ('US', 'United States of America');
