package com.mkcode.simplebanking.repositories;

import com.mkcode.simplebanking.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByAccountNo(String accountNo);
    List<Account> findAllByUserId(long userId);

}
