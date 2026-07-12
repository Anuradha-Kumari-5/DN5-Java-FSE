# PL/SQL — Stored Procedures Exercises

Three named, reusable PL/SQL stored procedures for a fictional bank,
built for Oracle Database.

---

## 1. Stored procedures vs. the anonymous blocks from Exercise 1

Exercise 1's scripts were **anonymous blocks** — `DECLARE ... BEGIN ...
END;` with no name, run once and discarded. A **stored procedure** is
the same kind of PL/SQL code, but saved permanently in the database
under a name (`CREATE OR REPLACE PROCEDURE <name> (...) IS ... END;`),
so it can be called repeatedly — from SQL*Plus, from an application,
from a scheduled job — without retyping the logic each time. It's the
difference between a one-off shopping list and a saved recipe you can
cook from again and again, this time with **parameters**
(`UpdateEmployeeBonus` and `TransferFunds` both take arguments), which
is what actually makes a "recipe" reusable for different inputs instead
of just for one fixed scenario.

---

## 2. Files

```
plsql-stored-procedures/
├── README.md
└── sql/
    ├── schema.sql                              (run first: creates accounts, employees)
    ├── sample_data.sql                         (run second: inserts test rows)
    ├── scenario1_process_monthly_interest.sql  (ProcessMonthlyInterest)
    ├── scenario2_update_employee_bonus.sql     (UpdateEmployeeBonus)
    └── scenario3_transfer_funds.sql            (TransferFunds)
```

Run `schema.sql` then `sample_data.sql` first. The three procedure
scripts can then be run/created in any order.

---

## 3. What each procedure does

### `ProcessMonthlyInterest` (no parameters)
Adds 1% interest to the `balance` of every account where
`account_type = 'SAVINGS'`. Unlike Exercise 1's row-by-row loops, this
uses a single set-based `UPDATE ... WHERE account_type = 'SAVINGS'` —
there's no per-row branching needed, since every matching row gets the
identical calculation, so one statement does the whole job more simply
and efficiently than looping and updating one row at a time.
```sql
EXEC ProcessMonthlyInterest;
```

### `UpdateEmployeeBonus(p_department, p_bonus_percentage)`
Adds a bonus (as a percentage of current salary) to every employee in
the given department. Validates the bonus percentage isn't negative or
null before touching any rows, and reports how many employees were
affected (or that none were found in that department).
```sql
EXEC UpdateEmployeeBonus('Engineering', 10);   -- 10% bonus for Engineering
```

### `TransferFunds(p_from_account, p_to_account, p_amount)`
Moves `p_amount` from one account to another, after confirming the
source account actually has enough balance. This one has the most
going on, worth walking through:

- **Row locking (`SELECT ... FOR UPDATE`)** — the source account's row
  is locked for the duration of the transaction before its balance is
  even checked. This matters under concurrency: without the lock, two
  simultaneous transfers from the same account could each read the
  same starting balance, each independently pass the sufficient-funds
  check, and both proceed — overdrawing the account even though each
  transfer looked valid in isolation. Locking serializes access so
  the second transfer sees the *already-debited* balance.
- **Validation before any data changes** — amount must be positive,
  source and destination accounts must differ, checked via named
  user-defined exceptions (`e_invalid_amount`, `e_same_account`)
  before any `UPDATE` runs.
- **Named exception handlers** — `NO_DATA_FOUND` (source account
  doesn't exist), plus the three user-defined exceptions, each produce
  a distinct, readable error via `RAISE_APPLICATION_ERROR` with its own
  error code (`-20003` through `-20006`) rather than one generic
  failure message.
- **Nothing commits until both updates succeed** — if the destination
  account turns out not to exist (caught via `SQL%ROWCOUNT = 0` after
  the credit `UPDATE`), the whole transaction — including the debit
  that already happened — rolls back, since `COMMIT` is the very last
  statement in the success path.

```sql
EXEC TransferFunds(1001, 1002, 5000);       -- succeeds
EXEC TransferFunds(1005, 1001, 1000000);    -- fails: insufficient funds
```

---

## How to run these

Oracle Database access required (Oracle XE works for local testing),
via SQL*Plus, SQL Developer, or an equivalent client.

```
sqlplus your_user/your_password@your_db

SQL> @schema.sql
SQL> @sample_data.sql
SQL> @scenario1_process_monthly_interest.sql
SQL> @scenario2_update_employee_bonus.sql
SQL> @scenario3_transfer_funds.sql

SQL> SET SERVEROUTPUT ON;
SQL> EXEC ProcessMonthlyInterest;
SQL> EXEC UpdateEmployeeBonus('Sales', 5);
SQL> EXEC TransferFunds(1001, 1002, 5000);

-- Check the results directly:
SQL> SELECT * FROM accounts;
SQL> SELECT * FROM employees;
```

`SET SERVEROUTPUT ON` is required for `DBMS_OUTPUT.PUT_LINE` messages
to actually display — without it, each procedure still runs correctly,
just silently.
