package com.mkcode.simplebanking.service;

import com.mkcode.simplebanking.model.Account;
import com.mkcode.simplebanking.model.Operation;
import com.mkcode.simplebanking.model.User;
import com.mkcode.simplebanking.repositories.AccountRepository;
import com.mkcode.simplebanking.repositories.OperationRepository;
import com.mkcode.simplebanking.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;

@Service
public class AccountsService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;

    public AccountsService(AccountRepository repository,
                           OperationRepository operationRepository,
                           UserRepository userRepository) {
        this.accountRepository = repository;
        this.operationRepository = operationRepository;
        this.userRepository = userRepository;
    }

    public List<Account> getUserAccounts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(format("User '%s' does not exists", username)));
        return accountRepository.findAllByUserId(user.getUserId());
    }

    public Account getAccount(String accountNo) {
        return accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new IllegalArgumentException(format("Account with no %s does not exists", accountNo)));
    }

    public BigDecimal getAccountBalance(String accountNo) {
        Account account = getAccount(accountNo);
        return operationRepository.getAccountBalance(account.getAccountId());
    }

    public List<Operation> getAccountOperations(String accountNo) {
        Account account = getAccount(accountNo);
        return operationRepository.findAllByAccountId(account.getAccountId());
    }

    public Operation postOperation(String accountNo, Operation operation) {
        Account account = getAccount(accountNo);
        operation.setAccountId(account.getAccountId());
        return operationRepository.save(operation);
    }
}