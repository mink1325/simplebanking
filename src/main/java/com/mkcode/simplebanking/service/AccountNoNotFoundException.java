package com.mkcode.simplebanking.service;

import com.mkcode.simplebanking.model.Account;

import static java.lang.String.format;

public class AccountNoNotFoundException extends RuntimeException {
    public AccountNoNotFoundException(String accountNo) {
        super(format("Account with no %s does not exists", accountNo));
    }
}