package com.mkcode.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ACCOUNTS_OPERATIONS")
@EntityListeners(AuditingEntityListener.class)
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long operationId;
    @Enumerated(EnumType.ORDINAL)
    private OperationType operationType;
    private BigDecimal amount;
    @CreatedDate
    private Instant dateCreated;

    @JsonIgnore
    private long accountId;

    public Operation() {
    }

    public Operation(OperationType operationType, BigDecimal amount, long accountId) {
        this.operationType = operationType;
        this.amount = amount;
        this.accountId = accountId;
    }

    public long getOperationId() {
        return operationId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
