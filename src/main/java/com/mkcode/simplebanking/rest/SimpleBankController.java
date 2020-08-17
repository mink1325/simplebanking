package com.mkcode.simplebanking.rest;


import com.mkcode.simplebanking.model.Account;
import com.mkcode.simplebanking.model.Operation;
import com.mkcode.simplebanking.service.AccountNoNotFoundException;
import com.mkcode.simplebanking.service.AccountsService;
import com.mkcode.simplebanking.service.OperationValidationException;
import com.mkcode.simplebanking.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Validated
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
                                          @Valid @RequestBody Operation operation,
                                          Principal principal) {
        return accountsService.postOperation(getUserId(principal), accountNo, operation);
    }

    private long getUserId(Principal principal) {
        return usersService.getUserId(principal.getName());
    }

    @ExceptionHandler({AccountNoNotFoundException.class, UsernameNotFoundException.class, OperationValidationException.class})
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<String>> handleException(MethodArgumentNotValidException exception) {
        List<String> errorDetails = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(toList());

        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
