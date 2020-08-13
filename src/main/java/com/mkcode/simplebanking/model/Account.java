package com.mkcode.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNTS")
public class Account {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;
    @Column(length = 20, unique = true)
    private String accountNo;
    @JsonIgnore
    private long userId;

    public long getAccountId() {
        return accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }
}