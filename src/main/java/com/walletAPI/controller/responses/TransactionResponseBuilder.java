package com.walletAPI.controller.responses;

import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.Transaction;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Map;

public interface TransactionResponseBuilder {
    /* This methods are used to build the HTTP responses for the Transaction Controller */

    public Map<String, Object> buildGetTransactionResponse(Page<Transaction> transactions
            , Account account, Date from, Date to, int page);
}
