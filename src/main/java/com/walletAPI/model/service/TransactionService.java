package com.walletAPI.model.service;

import com.walletAPI.model.entities.Transaction;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface TransactionService {

    public Transaction create(Transaction transaction);

    public void delete(Long idTransaction);

    public Transaction update(Transaction oldTransaction, Transaction newTransaction);

    public Transaction getTransactionById(Long idTransaction, Long idAccount);

    public Page<Transaction> getTransactionsFromTo(Long idAccount, Date from, Date to, int page, int size);

}
