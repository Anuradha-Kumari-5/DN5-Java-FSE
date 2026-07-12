-- Scenario 3: fetch all loans due within the next 30 days and print a
-- reminder message for each one.
--
-- This is read-only -- no UPDATE/COMMIT needed, since it only reports.

SET SERVEROUTPUT ON;

DECLARE
    v_days_ahead   CONSTANT NUMBER := 30;
    v_reminder_cnt NUMBER := 0;
BEGIN
    FOR loan_rec IN (
        SELECT l.loan_id, l.due_date, l.interest_rate, c.customer_id, c.name
        FROM loans l
        JOIN customers c ON c.customer_id = l.customer_id
        WHERE l.due_date BETWEEN SYSDATE AND SYSDATE + v_days_ahead
        ORDER BY l.due_date
    ) LOOP

        DBMS_OUTPUT.PUT_LINE(
            'Reminder: Dear ' || loan_rec.name || ', your loan (ID: ' || loan_rec.loan_id
            || ') of interest rate ' || loan_rec.interest_rate || '% is due on '
            || TO_CHAR(loan_rec.due_date, 'DD-MON-YYYY') || '.'
        );

        v_reminder_cnt := v_reminder_cnt + 1;

    END LOOP;

    IF v_reminder_cnt = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No loans are due within the next ' || v_days_ahead || ' days.');
    END IF;
END;
/
