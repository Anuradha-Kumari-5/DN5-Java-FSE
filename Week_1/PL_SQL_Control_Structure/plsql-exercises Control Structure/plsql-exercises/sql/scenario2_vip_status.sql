-- Scenario 2: promote a customer to VIP status (is_vip = 'Y') if their
-- balance is over $10,000.

SET SERVEROUTPUT ON;

DECLARE
    v_vip_threshold CONSTANT NUMBER := 10000;
BEGIN
    FOR cust_rec IN (
        SELECT customer_id, name, balance, is_vip
        FROM customers
    ) LOOP

        -- IF/THEN is the basic control structure here: only customers
        -- clearing the threshold get updated; everyone else is simply
        -- skipped (their existing is_vip value is left untouched).
        IF cust_rec.balance > v_vip_threshold THEN

            UPDATE customers
               SET is_vip = 'Y'
             WHERE customer_id = cust_rec.customer_id;

            DBMS_OUTPUT.PUT_LINE(
                'Customer ' || cust_rec.name || ' (ID: ' || cust_rec.customer_id
                || ') promoted to VIP. Balance: ' || cust_rec.balance
            );

        END IF;

    END LOOP;

    COMMIT;
END;
/
