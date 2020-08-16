package com.mkcode.simplebanking.service;

import com.mkcode.simplebanking.model.Account;
import com.mkcode.simplebanking.model.Operation;
import com.mkcode.simplebanking.model.OperationType;
import com.mkcode.simplebanking.repositories.AccountRepository;
import com.mkcode.simplebanking.repositories.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.mkcode.simplebanking.model.OperationType.DEPOSIT;
import static com.mkcode.simplebanking.model.OperationType.WITHDRAW;
import static java.lang.String.format;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountsServiceTest {
    private static final long USER_ID = 123L;
    private static final long ACCOUNT_ID = 1L;
    private static final String ACCOUNT_NO = "LT123456789012345678";
    private static final long ACCOUNT_ID_2 = 2L;
    private static final String ACCOUNT_NO_2 = "LT123456789123456789";
    private static final long ACCOUNT_ID_3 = 3L;
    private static final String ACCOUNT_NO_3 = "LT331234567890123456";
    @Mock
    AccountRepository accountRepository;
    @Mock
    OperationRepository operationRepository;

    AccountsService accountsService;

    @BeforeEach
    void setup() {
        accountsService = new AccountsService(accountRepository, operationRepository);
    }

    @Test
    void givenUserThatHaveAccounts_whenGettingUserAccounts_thenUserAccountsAreReturned() {
        ArrayList<Account> userAccounts = newArrayList(createAccountFixture(ACCOUNT_NO, ACCOUNT_ID),
                createAccountFixture(ACCOUNT_NO_2, ACCOUNT_ID_2));
        doReturn(userAccounts).when(accountRepository).findAllByUserId(USER_ID);

        assertThat(accountsService.getUserAccounts(USER_ID))
                .containsExactlyInAnyOrderElementsOf(userAccounts);
    }

    @Test
    void givenUserThatHasNoAccount_whenGettingUserAccounts_thenEmptyListIsReturned() {
        doReturn(newArrayList()).when(accountRepository).findAllByUserId(USER_ID);

        assertThat(accountsService.getUserAccounts(USER_ID)).hasSize(0);
    }

    @Test
    void givenUserIdAndAccountNo_whenGettingAccountInfo_thenAccountInfoIsReturned() {
        doReturn(Optional.of(createAccountFixture(ACCOUNT_NO, ACCOUNT_ID))).when(accountRepository)
                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO);

        Account account = accountsService.getUserAccount(USER_ID, ACCOUNT_NO);
        assertThat(account.getAccountNo()).isEqualTo(ACCOUNT_NO);
    }

    @Test
    void givenUserIdAndAccountNoThatDoesNotExist_whenGettingAccountInfo_thenExceptionIsThrown() {
        doReturn(Optional.empty()).when(accountRepository)
                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO_2);

        Throwable throwable = catchThrowable(() -> accountsService.getUserAccount(USER_ID, ACCOUNT_NO_2));
        assertThat(throwable)
                .isExactlyInstanceOf(AccountNoNotFoundException.class)
                .hasMessage(format("Account with no %s does not exists", ACCOUNT_NO_2));
    }

    @Test
    void givenAccountId_whenGettingAccountBalance_thenBalanceIsReturned() {
        doReturn(Optional.of(createAccountFixture(ACCOUNT_NO, ACCOUNT_ID))).when(accountRepository)
                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO);
        doReturn(Optional.of(TEN)).when(operationRepository).getAccountBalance(ACCOUNT_ID);

        BigDecimal accountBalance = accountsService.getAccountBalance(USER_ID, ACCOUNT_NO);
        assertThat(accountBalance).isEqualTo(TEN);
    }

    @Test
    void givenAccountWithoutOperations_whenGettingAccountBalance_thenZeroBalanceIsReturned() {
        doReturn(Optional.of(createAccountFixture(ACCOUNT_NO, ACCOUNT_ID))).when(accountRepository)
                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO);
        doReturn(Optional.empty()).when(operationRepository).getAccountBalance(ACCOUNT_ID);

        BigDecimal accountBalance = accountsService.getAccountBalance(USER_ID, ACCOUNT_NO);
        assertThat(accountBalance).isEqualTo(ZERO);
    }

    @Test
    void givenAccountId_whenGettingOperationList_thenOperationListIsReturned() {
        doReturn(Optional.of(createAccountFixture(ACCOUNT_NO, ACCOUNT_ID))).when(accountRepository)
                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO);
        ArrayList<Operation> accountOperations = newArrayList(createOperationFixture(ACCOUNT_ID, BigDecimal.valueOf(100.01)),
                createOperationFixture(ACCOUNT_ID, BigDecimal.valueOf(-11.11)));
        doReturn(accountOperations).when(operationRepository).findAllByAccountId(ACCOUNT_ID);

        List<Operation> operations = accountsService.getAccountOperations(USER_ID, ACCOUNT_NO);

        assertThat(operations).containsExactlyInAnyOrderElementsOf(accountOperations);
    }

    @Test
    void givenOperation_whenPostingOperation_thenOperationIsSaved() {
        doReturn(Optional.of(createAccountFixture(ACCOUNT_NO, ACCOUNT_ID))).when(accountRepository)
                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO);
        BigDecimal amount = BigDecimal.valueOf(11.11);

        Operation postingOperation = new Operation();
        postingOperation.setOperationType(DEPOSIT);
        postingOperation.setAmount(amount);
        when(operationRepository.save(postingOperation)).thenReturn(postingOperation);

        Operation operation = accountsService.postOperation(USER_ID, ACCOUNT_NO, postingOperation);

        verify(operationRepository).save(operation);

        assertThat(operation.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(operation.getAmount()).isEqualTo(amount);
        assertThat(operation.getOperationType()).isEqualTo(DEPOSIT);
    }

    @Test
    void givenDepositOperationWithNegativeAmount_whenPostingOperation_thenExceptionIsThrow() {
        Operation operation = createOperationFixture(ACCOUNT_ID_3, BigDecimal.valueOf(-1), DEPOSIT);

        Throwable throwable = catchThrowable(() -> accountsService.validateOperation(USER_ID, ACCOUNT_NO_3, operation));

        assertThat(throwable)
                .isInstanceOf(OperationValidationException.class)
                .hasMessage("Amount must be positive for DEPOSIT type operation.");
    }

    @Test
    void givenWithdrawOperationWithPositiveAmount_whenPostingOperation_thenExceptionIsThrow() {
        Operation operation = createOperationFixture(ACCOUNT_ID_3, BigDecimal.valueOf(11), WITHDRAW);

        Throwable throwable = catchThrowable(() -> accountsService.validateOperation(USER_ID, ACCOUNT_NO_3, operation));

        assertThat(throwable)
                .isInstanceOf(OperationValidationException.class)
                .hasMessage("Amount must be negative for WITHDRAW type operation.");
    }

    @Test
    void givenOperationWithZeroAmount_whenPostingOperation_thenExceptionIsThrow() {
        doReturn(Optional.of(createAccountFixture(ACCOUNT_NO_3, ACCOUNT_ID_3))).when(accountRepository)
                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO_3);
        Operation operation = createOperationFixture(ACCOUNT_ID_3, BigDecimal.valueOf(-1), WITHDRAW);

        Throwable throwable = catchThrowable(() -> accountsService.validateOperation(USER_ID, ACCOUNT_NO_3, operation));

        assertThat(throwable)
                .isInstanceOf(OperationValidationException.class)
                .hasMessage("Account balance should be zero or more.");
    }

    @Test
    void givenOperationMakingAccountBalanceNegative_whenPostingOperation_thenExceptionIsThrow() {
//        doReturn(Optional.of(createAccountFixture(ACCOUNT_NO_3, ACCOUNT_ID_3))).when(accountRepository)
//                .findByUserIdAndAccountNo(USER_ID, ACCOUNT_NO_3);
        Operation operation = createOperationFixture(ACCOUNT_ID_3, ZERO, WITHDRAW);

        Throwable throwable = catchThrowable(() -> accountsService.validateOperation(USER_ID, ACCOUNT_NO_3, operation));

        assertThat(throwable)
                .isInstanceOf(OperationValidationException.class)
                .hasMessage("Operation's amount can not be 0.");
    }

    private Account createAccountFixture(String accountNo, long accountId) {
        Account account = new Account();
        account.setAccountNo(accountNo);
        account.setAccountId(accountId);
        return account;
    }

    private Operation createOperationFixture(long accountId, BigDecimal amount) {
        return createOperationFixture(accountId, amount, amount.compareTo(ZERO) < 0 ? WITHDRAW : DEPOSIT);
    }

    private Operation createOperationFixture(long accountId, BigDecimal amount, OperationType operationType) {
        Operation operation = new Operation();
        operation.setAccountId(accountId);
        operation.setAmount(amount);
        operation.setOperationType(operationType);
        return operation;
    }
}