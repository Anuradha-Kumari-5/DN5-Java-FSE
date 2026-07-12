-- Scenario 1: apply a 1% discount to the loan interest rates of
-- customers over 60 years old.
--
-- Assumption worth stating explicitly: "1% discount" is implemented
-- here as subtracting 1 PERCENTAGE POINT from the current rate (e.g.
-- 8.50% -> 7.50%), which is the common banking-promo meaning of
-- "1% off your interest rate." An alternative reading -- reducing the
-- rate by 1% OF ITS CURRENT VALUE (e.g. 8.50% -> 8.415%) -- is shown
-- commented out below the main UPDATE, in case that's what's meant
-- instead. Worth confirming with your SME which one they intend.

SET SERVEROUTPUT ON;

DECLARE
    v_discount_points CONSTANT NUMBER := 1;  -- percentage POINTS to subtract
BEGIN
    -- A cursor FOR loop implicitly opens a cursor, fetches each row,
    -- and closes it when done -- no explicit OPEN/FETCH/CLOSE needed.
    -- cust_rec is a record whose fields match the SELECT list.
    FOR cust_rec IN (
        SELECT customer_id, name, age
        FROM customers
        WHERE age > 60
    ) LOOP

        UPDATE loans
           SET interest_rate = interest_rate - v_discount_points
         WHERE customer_id = cust_rec.customer_id
           AND interest_rate > v_discount_points;  -- guard against a rate dropping to zero or below

        -- Alternative interpretation (percentage OF current rate, not
        -- percentage points): uncomment this instead of the UPDATE
        -- above if that's the intended meaning.
        -- UPDATE loans
        --    SET interest_rate = ROUND(interest_rate * (1 - v_discount_points / 100), 2)
        --  WHERE customer_id = cust_rec.customer_id;

        DBMS_OUTPUT.PUT_LINE(
            'Applied ' || v_discount_points || '% discount to loan interest rate(s) for customer: '
            || cust_rec.name || ' (ID: ' || cust_rec.customer_id || ', Age: ' || cust_rec.age || ')'
        );

    END LOOP;

    COMMIT;
END;
/
