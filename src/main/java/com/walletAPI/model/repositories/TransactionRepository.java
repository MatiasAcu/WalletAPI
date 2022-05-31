package com.walletAPI.model.repositories;

import com.walletAPI.model.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    long deleteByIdTransaction(Long idTransaction);

    Optional<Transaction> findByAccount_IdAccountAndIdTransaction(Long idAccount, Long idTransaction);

    @Query("SELECT t from Transaction t " +
            "WHERE t.account.idAccount = :idAccount AND t.date BETWEEN :from AND :to ORDER BY t.date ASC")
    Page<Transaction> findTransactionsBetweenDates(Long idAccount, Date from, Date to, Pageable pageable);

    Optional<Transaction> findByIdTransactionAndAccount_IdAccount(Long idTransaction, Long idAccount);
}
