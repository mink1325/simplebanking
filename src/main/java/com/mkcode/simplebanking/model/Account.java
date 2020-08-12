package com.mkcode.simplebanking.model;

public class Account {
    private final String accountNo;
    private final double amount;

    public Account(String accountNo, double amount) {
        this.accountNo = accountNo;
        this.amount = amount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public double getAmount() {
        return amount;
    }
}