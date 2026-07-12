-- Sample data for testing all three scenarios.
-- Run schema.sql first.
--
-- Ages/balances/due dates are chosen deliberately so each scenario
-- script has both matching and non-matching rows to act on -- e.g.
-- some customers are over 60 and some aren't, some balances are above
-- $10,000 and some aren't, some loans are due within 30 days and some
-- aren't.

INSERT INTO customers (customer_id, name, age, balance, is_vip) VALUES (1, 'Anita Sharma',   65, 15000, 'N');
INSERT INTO customers (customer_id, name, age, balance, is_vip) VALUES (2, 'Rohit Verma',     45,  8000, 'N');
INSERT INTO customers (customer_id, name, age, balance, is_vip) VALUES (3, 'Meena Iyer',      72, 25000, 'N');
INSERT INTO customers (customer_id, name, age, balance, is_vip) VALUES (4, 'Sanjay Gupta',    38,  3000, 'N');
INSERT INTO customers (customer_id, name, age, balance, is_vip) VALUES (5, 'Priya Nair',      61,  9500, 'N');

-- interest_rate is a plain percentage (8.50 = 8.50%).
-- due_date is relative to SYSDATE so the "due within 30 days" scenario
-- always has something to find, however long after this script runs.
INSERT INTO loans (loan_id, customer_id, principal, interest_rate, due_date) VALUES (101, 1, 500000, 8.50, SYSDATE + 10);
INSERT INTO loans (loan_id, customer_id, principal, interest_rate, due_date) VALUES (102, 1, 200000, 9.00, SYSDATE + 90);
INSERT INTO loans (loan_id, customer_id, principal, interest_rate, due_date) VALUES (103, 2, 150000, 7.75, SYSDATE + 5);
INSERT INTO loans (loan_id, customer_id, principal, interest_rate, due_date) VALUES (104, 3, 300000, 8.25, SYSDATE + 25);
INSERT INTO loans (loan_id, customer_id, principal, interest_rate, due_date) VALUES (105, 4, 100000, 10.00, SYSDATE + 45);
INSERT INTO loans (loan_id, customer_id, principal, interest_rate, due_date) VALUES (106, 5, 250000, 9.50, SYSDATE + 29);

COMMIT;
