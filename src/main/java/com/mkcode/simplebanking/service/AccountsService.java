package com.mkcode.simplebanking.service;

import com.mkcode.simplebanking.model.Account;
import com.mkcode.simplebanking.model.Operation;
import com.mkcode.simplebanking.repositories.AccountRepository;
import com.mkcode.simplebanking.repositories.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;

@Service
public class AccountsService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountsService(AccountRepository repository,
                           OperationRepository operationRepository) {
        this.accountRepository = repository;
        this.operationRepository = operationRepository;
    }

    public List<Account> getUserAccounts(long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    public Account getUserAccount(long userId, String accountNo) {
        return accountRepository.findByUserIdAndAccountNo(userId, accountNo)
                .orElseThrow(() -> new AccountNoNotFoundException(accountNo));
    }

    public BigDecimal getAccountBalance(long userId, String accountNo) {
        return operationRepository.getAccountBalance(getUserAccountId(userId, accountNo)).orElse(ZERO);
    }

    public List<Operation> getAccountOperations(long userId, String accountNo) {
        return operationRepository.findAllByAccountId(getUserAccountId(userId, accountNo));
    }

    public Operation postOperation(long userId, String accountNo, Operation operation) {
        operation.setAccountId(getUserAccountId(userId, accountNo));
        return operationRepository.save(operation);
    }

    private long getUserAccountId(long userId, String accountNo) {
        return getUserAccount(userId, accountNo).getAccountId();
    }
}