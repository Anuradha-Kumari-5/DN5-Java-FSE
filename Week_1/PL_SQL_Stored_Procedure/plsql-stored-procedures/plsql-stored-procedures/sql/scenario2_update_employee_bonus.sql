-- Scenario 2: add a bonus percentage (passed in as a parameter) to the
-- salary of every employee in a given department.

CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_department       IN employees.department%TYPE,
    p_bonus_percentage IN NUMBER   -- e.g. 5 means a 5% bonus
)
IS
BEGIN
    -- Basic input validation before touching any data.
    IF p_bonus_percentage IS NULL OR p_bonus_percentage < 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Bonus percentage must be a non-negative number.');
    END IF;

    UPDATE employees
       SET salary = salary + (salary * p_bonus_percentage / 100)
     WHERE department = p_department;

    IF SQL%ROWCOUNT = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No employees found in department: ' || p_department);
    ELSE
        DBMS_OUTPUT.PUT_LINE(
            SQL%ROWCOUNT || ' employee(s) in the ' || p_department
            || ' department received a ' || p_bonus_percentage || '% bonus.'
        );
    END IF;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('UpdateEmployeeBonus failed: ' || SQLERRM);
        RAISE;
END UpdateEmployeeBonus;
/

-- To run it, e.g. a 10% bonus for everyone in Engineering:
-- SET SERVEROUTPUT ON;
-- EXEC UpdateEmployeeBonus('Engineering', 10);
