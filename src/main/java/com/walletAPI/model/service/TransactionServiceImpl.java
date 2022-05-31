package com.walletAPI.model.service;

import com.walletAPI.model.entities.Transaction;
import com.walletAPI.model.repositories.TransactionRepository;
import com.walletAPI.model.service.exceptions.TransactionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction create(Transaction transaction) {
        //If date is null creates new date with current time at UTC-0
        if (transaction.getDate() == null) {

            transaction.setDate(Date.from(OffsetDateTime.ofInstant(Instant.now(), ZoneId.of("UTC+0")).toInstant()));

        }
        return transactionRepository.save(transaction);
    }

    @Override
    public void delete(Long idTransaction) {
        if (transactionRepository.deleteByIdTransaction(idTransaction) == 0) {
            throw new TransactionException("{ \"errorCode\": \"TRANSACTION.SERVICE.NOT_FOUND\", \"message\": \"Transaction not found\"}");
        }
    }

    @Override
    public Transaction update(Transaction oldTransaction, Transaction newTransaction) {
        oldTransaction.setDate(newTransaction.getDate());
        oldTransaction.setAmount(newTransaction.getAmount());
        oldTransaction.setDescription(newTransaction.getDescription());
        return transactionRepository.save(oldTransaction);
    }

    @Override
    public Transaction getTransactionById(Long idTransaction, Long idAccount) {
        return transactionRepository.findByIdTransactionAndAccount_IdAccount(idTransaction, idAccount)
                .orElseThrow(() -> new TransactionException("{ \"errorCode\": \"TRANSACTION.SERVICE.NOT_FOUND\", \"message\": \"Transaction not found\"}"));
    }

    @Override
    public Page<Transaction> getTransactionsFromTo(Long idAccount, Date from, Date to, int page, int size) {
        return transactionRepository.findTransactionsBetweenDates(idAccount, from, to, PageRequest.of(page, size));
    }
}
