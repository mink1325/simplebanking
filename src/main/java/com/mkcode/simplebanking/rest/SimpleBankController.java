package com.mkcode.simplebanking.rest;


import com.mkcode.simplebanking.model.Account;
import com.mkcode.simplebanking.model.Operation;
import com.mkcode.simplebanking.service.AccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController("/")
public class SimpleBankController {

    private final AccountsService accountsService;

    public SimpleBankController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping(path = "/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getUserAccounts(Principal principal) {
        return accountsService.getUserAccounts(principal.getName());
    }

    @GetMapping(path = "/accounts/{accountNo}/balance")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getAccountBalance(@PathVariable("accountNo") String accountNo) {
        return accountsService.getAccountBalance(accountNo);
    }

    @GetMapping(path = "/accounts/{accountNo}/operations")
    @ResponseStatus(HttpStatus.OK)
    public List<Operation> getAccountOperations(@PathVariable("accountNo") String accountNo) {
        return accountsService.getAccountOperations(accountNo);
    }

    @PostMapping(path = "/accounts/{accountNo}/operations")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Operation postAccountOperation(@PathVariable("accountNo") String accountNo,
                                          @RequestBody Operation operation) {
        return accountsService.postOperation(accountNo, operation);
    }
}
