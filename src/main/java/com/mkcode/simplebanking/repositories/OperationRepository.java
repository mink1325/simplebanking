package com.mkcode.simplebanking.repositories;

import com.mkcode.simplebanking.model.Operation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Long> {

    List<Operation> findAllByAccountId(long accountId);

    @Query("SELECT SUM(o.amount) FROM Operation o WHERE o.accountId = :accountId")
    Optional<BigDecimal> getAccountBalance(@Param("accountId") long accountId);
}
