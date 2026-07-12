# PL/SQL — Control Structures Exercises

Three standalone PL/SQL anonymous blocks for a fictional bank's
customer/loan management, built for Oracle Database (PL/SQL is
Oracle's procedural extension to SQL — plain SQL alone has no loops or
if/else branching, which is exactly what these exercises are about).

---

## 1. The idea, in plain terms

Plain SQL is good at describing *what* you want ("give me all customers
over 60") but has no built-in way to describe *steps* — loop through
these, check a condition, do something different depending on the
result. PL/SQL adds that procedural layer on top of SQL, the same way a
recipe adds "repeat until golden brown" and "if the dough is too dry,
add a tablespoon of water" on top of a plain ingredients list.

Every block here follows the same shape:
```sql
DECLARE
    -- variables/constants go here
BEGIN
    FOR row IN (SELECT ...) LOOP
        IF <condition> THEN
            -- do something
        END IF;
    END LOOP;
END;
/
```
- `DECLARE` — variables and constants used inside the block.
- A **cursor `FOR` loop** (`FOR row IN (SELECT ...) LOOP ... END LOOP;`)
  is the simplest way to iterate row-by-row over a query's results —
  Oracle opens the cursor, fetches each row into `row`, and closes it
  automatically; there's no explicit `OPEN`/`FETCH`/`CLOSE` to manage
  by hand.
- `IF ... THEN ... END IF;` is the basic branching control structure —
  only rows meeting the condition get acted on.
- The trailing `/` on its own line tells SQL*Plus/SQL Developer "this
  is the end of the PL/SQL block, run it now."

---

## 2. Files in this repo

```
plsql-exercises/
├── README.md
└── sql/
    ├── schema.sql                        (run first: creates customers, loans)
    ├── sample_data.sql                   (run second: inserts test rows)
    ├── scenario1_senior_discount.sql     (age > 60 -> 1% off loan interest rate)
    ├── scenario2_vip_status.sql          (balance > $10,000 -> is_vip = 'Y')
    └── scenario3_loan_reminders.sql      (loans due in next 30 days -> printed reminders)
```

Run them in that order — `schema.sql`, then `sample_data.sql`, then any
of the three scenario scripts, in any order (they're independent of
each other).

---

## 3. What each scenario does

### Scenario 1 — senior citizen interest rate discount
Loops through `customers` where `age > 60`, and for each one, updates
every one of their loans by subtracting 1 (percentage point) from
`interest_rate`, guarded so a rate never drops to zero or below.

**Worth flagging:** "apply a 1% discount" is genuinely ambiguous —
it could mean *subtract 1 percentage point* (8.50% → 7.50%, the
interpretation used here, and the common phrasing in real bank
promotions) or *reduce the rate by 1% of its current value*
(8.50% → 8.415%, a much smaller change). The script includes the
second version commented out directly below the main `UPDATE`, so
switching interpretations is a one-line change if your SME means the
other one.

### Scenario 2 — VIP promotion
Loops through all customers; any customer with `balance > 10000` gets
`is_vip` set to `'Y'`. Note `'Y'`/`'N'`, not a native SQL `BOOLEAN` —
see the schema note below.

### Scenario 3 — loan due-date reminders
Read-only: joins `loans` to `customers`, filters to
`due_date BETWEEN SYSDATE AND SYSDATE + 30`, and prints one reminder
line per matching loan via `DBMS_OUTPUT.PUT_LINE`. If nothing matches,
it prints a "no loans due soon" message instead, using a counter
variable incremented inside the loop (a `SELECT`-driven cursor loop
doesn't populate `SQL%ROWCOUNT` the way an `UPDATE`/`DELETE` would, so
counting manually is the correct approach here).

---

## 4. Why `IsVIP` is `VARCHAR2(1)`, not `BOOLEAN`

Oracle table **columns** cannot be declared as `BOOLEAN` — that data
type only exists for variables *inside* PL/SQL code (and even there,
it can't be selected into directly from a table). `BOOLEAN` as a column
type only arrived in Oracle 23c, and even then it's not yet the common
convention. The long-standing, portable Oracle pattern for a true/false
flag column is `VARCHAR2(1)` (or sometimes `NUMBER(1)`) storing `'Y'`/
`'N'` — which is what `schema.sql` uses, with a `CHECK` constraint
restricting it to exactly those two values.

---

## How to run these

You'll need access to an Oracle Database instance (Oracle XE is a free
option for local testing) and either SQL*Plus, SQL Developer, or any
SQL client that supports PL/SQL blocks.

```
sqlplus your_user/your_password@your_db

SQL> @schema.sql
SQL> @sample_data.sql
SQL> @scenario1_senior_discount.sql
SQL> @scenario2_vip_status.sql
SQL> @scenario3_loan_reminders.sql
```

Each scenario script has `SET SERVEROUTPUT ON;` at the top, which is
required for `DBMS_OUTPUT.PUT_LINE` messages to actually display in
your client — without it, the block still runs, but silently.

To re-run scenario 1 or 2 more than once and see fresh output, re-run
`sample_data.sql` first (after `DELETE FROM loans; DELETE FROM
customers; COMMIT;` to clear old rows) so the discount/VIP changes
don't compound or get skipped as already-applied.
