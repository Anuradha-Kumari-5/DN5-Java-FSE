-- Scenario 1: apply 1% monthly interest to every savings account's
-- balance.
--
-- This uses a single set-based UPDATE rather than a row-by-row loop
-- (unlike the Exercise 1 control-structures scripts) because there's
-- no per-row branching needed here -- every SAVINGS account gets the
-- exact same calculation, so one UPDATE ... WHERE statement does the
-- whole job faster and more simply than looping and updating one row
-- at a time. This is a common PL/SQL principle: prefer set-based SQL
-- over row-by-row processing unless you genuinely need per-row logic.

CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest
IS
    c_interest_rate CONSTANT NUMBER := 0.01;  -- 1%
BEGIN
    UPDATE accounts
       SET balance = balance + (balance * c_interest_rate)
     WHERE account_type = 'SAVINGS';

    DBMS_OUTPUT.PUT_LINE(
        SQL%ROWCOUNT || ' savings account(s) updated with 1% monthly interest.'
    );

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ProcessMonthlyInterest failed: ' || SQLERRM);
        RAISE;
END ProcessMonthlyInterest;
/

-- To run it:
-- SET SERVEROUTPUT ON;
-- EXEC ProcessMonthlyInterest;
