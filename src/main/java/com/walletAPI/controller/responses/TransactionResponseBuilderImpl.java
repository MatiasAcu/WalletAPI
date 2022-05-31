package com.walletAPI.controller.responses;

import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionResponseBuilderImpl implements TransactionResponseBuilder {

    @Override
    public Map<String, Object> buildGetTransactionResponse(Page<Transaction> transactions
            , Account account, Date from, Date to, int page) {

        Map<String, Object> accountBody = new HashMap<>();
        accountBody.put("name", account.getName());
        accountBody.put("transactions", transactions.getContent());

        Map<String, Object> body = new HashMap<>();
        body.put("account", accountBody);
        body.put("totalElements", transactions.getTotalElements());
        body.put("totalPages", transactions.getTotalPages());
        body.put("page", page);
        body.put("from", from.toInstant());
        body.put("to", to.toInstant());

        return body;
    }
}
