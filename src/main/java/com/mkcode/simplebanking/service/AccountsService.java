package com.mkcode.simplebanking.service;

import com.mkcode.simplebanking.model.Account;
import com.mkcode.simplebanking.model.Operation;
import com.mkcode.simplebanking.repositories.AccountRepository;
import com.mkcode.simplebanking.repositories.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.mkcode.simplebanking.model.OperationType.DEPOSIT;
import static com.mkcode.simplebanking.model.OperationType.WITHDRAW;
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
        validateOperation(userId, accountNo, operation);
        return operationRepository.save(operation);
    }

    void validateOperation(long userId, String accountNo, Operation operation) {
        if (operation.getAmount().equals(ZERO)) {
            throw new OperationValidationException("Operation's amount can not be 0.");
        } else if (operation.getAmount().compareTo(ZERO) < 0 && DEPOSIT.equals(operation.getOperationType())) {
            throw new OperationValidationException("Amount must be positive for DEPOSIT type operation.");
        } else if (operation.getAmount().compareTo(ZERO) > 0 && WITHDRAW.equals(operation.getOperationType())) {
            throw new OperationValidationException("Amount must be negative for WITHDRAW type operation.");
        } else if (getAccountBalance(userId, accountNo).add(operation.getAmount()).compareTo(ZERO) < 0) {
            throw new OperationValidationException("Account balance should be zero or more.");
        }
    }

    private long getUserAccountId(long userId, String accountNo) {
        return getUserAccount(userId, accountNo).getAccountId();
    }
}