package com.mkcode.simplebanking.rest;


import com.mkcode.simplebanking.model.Account;
import com.mkcode.simplebanking.model.Operation;
import com.mkcode.simplebanking.service.AccountsService;
import com.mkcode.simplebanking.service.UsersService;
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
    private final UsersService usersService;

    public SimpleBankController(AccountsService accountsService, UsersService usersService) {
        this.accountsService = accountsService;
        this.usersService = usersService;
    }

    @GetMapping(path = "/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getUserAccounts(Principal principal) {
        return accountsService.getUserAccounts(getUserId(principal));
    }

    @GetMapping(path = "/accounts/{accountNo}/balance")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getAccountBalance(@PathVariable("accountNo") String accountNo, Principal principal) {
        return accountsService.getAccountBalance(getUserId(principal), accountNo);
    }

    @GetMapping(path = "/accounts/{accountNo}/operations")
    @ResponseStatus(HttpStatus.OK)
    public List<Operation> getAccountOperations(@PathVariable("accountNo") String accountNo, Principal principal) {
        return accountsService.getAccountOperations(getUserId(principal), accountNo);
    }

    @PostMapping(path = "/accounts/{accountNo}/operations")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Operation postAccountOperation(@PathVariable("accountNo") String accountNo,
                                          @RequestBody Operation operation,
                                          Principal principal) {
        return accountsService.postOperation(getUserId(principal), accountNo, operation);
    }

    private long getUserId(Principal principal) {
        return usersService.getUserId(principal.getName());
    }
}
