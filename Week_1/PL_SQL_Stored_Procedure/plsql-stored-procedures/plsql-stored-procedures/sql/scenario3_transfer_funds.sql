-- Scenario 3: transfer a specified amount from one account to another,
-- checking the source account has sufficient balance first.

CREATE OR REPLACE PROCEDURE TransferFunds (
    p_from_account IN accounts.account_id%TYPE,
    p_to_account   IN accounts.account_id%TYPE,
    p_amount       IN NUMBER
)
IS
    v_from_balance       accounts.balance%TYPE;
    e_insufficient_funds EXCEPTION;
    e_invalid_amount     EXCEPTION;
    e_same_account       EXCEPTION;
BEGIN
    IF p_amount IS NULL OR p_amount <= 0 THEN
        RAISE e_invalid_amount;
    END IF;

    IF p_from_account = p_to_account THEN
        RAISE e_same_account;
    END IF;

    -- SELECT ... FOR UPDATE locks the source account's row for the
    -- rest of this transaction. This matters for correctness under
    -- concurrency: without it, two simultaneous transfers could both
    -- read the same starting balance, both pass the sufficient-funds
    -- check, and both debit the account -- overdrawing it even though
    -- each transfer individually looked valid. NO_DATA_FOUND fires
    -- here automatically if p_from_account doesn't exist at all.
    SELECT balance
      INTO v_from_balance
      FROM accounts
     WHERE account_id = p_from_account
       FOR UPDATE;

    IF v_from_balance < p_amount THEN
        RAISE e_insufficient_funds;
    END IF;

    UPDATE accounts
       SET balance = balance - p_amount
     WHERE account_id = p_from_account;

    UPDATE accounts
       SET balance = balance + p_amount
     WHERE account_id = p_to_account;

    IF SQL%ROWCOUNT = 0 THEN
        -- The destination account doesn't exist. Nothing has been
        -- committed yet, so raising here and rolling back in the
        -- exception handler below correctly undoes the debit too.
        RAISE_APPLICATION_ERROR(-20002, 'Destination account ' || p_to_account || ' does not exist.');
    END IF;

    COMMIT;

    DBMS_OUTPUT.PUT_LINE(
        'Transferred ' || p_amount || ' from account ' || p_from_account
        || ' to account ' || p_to_account || '.'
    );

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20003, 'Source account ' || p_from_account || ' does not exist.');
    WHEN e_insufficient_funds THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20004,
            'Insufficient balance in account ' || p_from_account || ' to transfer ' || p_amount || '.');
    WHEN e_invalid_amount THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20005, 'Transfer amount must be a positive number.');
    WHEN e_same_account THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20006, 'Source and destination accounts must be different.');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END TransferFunds;
/

-- To run it, e.g. transfer 5000 from account 1001 to account 1002:
-- SET SERVEROUTPUT ON;
-- EXEC TransferFunds(1001, 1002, 5000);
--
-- To see the insufficient-funds error path:
-- EXEC TransferFunds(1005, 1001, 1000000);
