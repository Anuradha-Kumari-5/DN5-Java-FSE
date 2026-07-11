package com.cognizant.account.model;

/**
 * Plain response object for an account. This service has no backend/
 * database connectivity -- every field except "number" is a fixed dummy
 * value, as the exercise specifies.
 */
public class Account {

    private String number;
    private String type;
    private long balance;

    public Account() {
    }

    public Account(String number, String type, long balance) {
        this.number = number;
        this.type = type;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

}
