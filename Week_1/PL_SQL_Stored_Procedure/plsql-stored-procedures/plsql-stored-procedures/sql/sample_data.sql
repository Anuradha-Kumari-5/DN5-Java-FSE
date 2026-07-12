-- Sample data for testing all three stored procedures.
-- Run schema.sql first.

INSERT INTO accounts (account_id, customer_name, account_type, balance) VALUES (1001, 'Anita Sharma', 'SAVINGS',  50000);
INSERT INTO accounts (account_id, customer_name, account_type, balance) VALUES (1002, 'Rohit Verma',  'SAVINGS',  12000);
INSERT INTO accounts (account_id, customer_name, account_type, balance) VALUES (1003, 'Meena Iyer',   'CURRENT',  30000);
INSERT INTO accounts (account_id, customer_name, account_type, balance) VALUES (1004, 'Anita Sharma', 'CURRENT',   8000);
INSERT INTO accounts (account_id, customer_name, account_type, balance) VALUES (1005, 'Sanjay Gupta', 'SAVINGS',   2000);

INSERT INTO employees (employee_id, name, department, salary) VALUES (1, 'Divya Rao',    'Sales',       55000);
INSERT INTO employees (employee_id, name, department, salary) VALUES (2, 'Arjun Mehta',  'Sales',       60000);
INSERT INTO employees (employee_id, name, department, salary) VALUES (3, 'Kavita Joshi', 'Engineering', 90000);
INSERT INTO employees (employee_id, name, department, salary) VALUES (4, 'Farhan Khan',  'Engineering', 95000);
INSERT INTO employees (employee_id, name, department, salary) VALUES (5, 'Neha Kapoor',  'Marketing',   58000);

COMMIT;
