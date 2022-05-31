package com.walletAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.walletAPI.controller.responses.TransactionResponseBuilder;
import com.walletAPI.json.JsonPatcher;
import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.Transaction;
import com.walletAPI.model.entities.User;
import com.walletAPI.model.service.AccountService;
import com.walletAPI.model.service.TransactionService;
import com.walletAPI.model.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.walletAPI.controller.AccountController.ACCOUNT_URL;


@RestController
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;

    private final UserService userService;

    private final AccountService accountService;

    private final JsonPatcher<Transaction> jsonPatcher;

    private final TransactionResponseBuilder responseBuilder;

    public TransactionControllerImpl(TransactionService transactionService, UserService userService, AccountService accountService, JsonPatcher<Transaction> jsonPatcher, TransactionResponseBuilder responseBuilder) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.accountService = accountService;
        this.jsonPatcher = jsonPatcher;
        this.responseBuilder = responseBuilder;
    }


    @Override
    public ResponseEntity<Object> create(Transaction transaction,
                                         String accountName,
                                         Authentication authentication) {

        String username = authentication.getName(); //Finds session user
        User sessionUser = userService.getUser(username);

        Account account = accountService.getAccountByName(sessionUser, accountName); //Finds the account

        transaction.setAccount(account); //Attaches the account to the new transaction
        transaction = transactionService.create(transaction); //Saves the transaction

        return ResponseEntity
                .created( //Returns URI to access new transaction and the transaction data as body
                        URI.create(
                                ACCOUNT_URL + "/" + accountName
                                        + "/transactions/" + transaction.getIdTransaction()))
                .body(transaction.toString());


    }

    @Override
    public ResponseEntity<Object> delete(String accountName,
                                         Long transactionsId,
                                         Authentication authentication) {

        String username = authentication.getName();
        User sessionUser = userService.getUser(username); //Finds session user

        Account account = accountService.getAccountByName(sessionUser, accountName); //Finds the account


        transactionService.delete(transactionsId); //Deletes the transaction

        return ResponseEntity.ok("Transaction deleted successfully");

    }

    @Override
    public ResponseEntity<Object> update(String accountName,
                                         Long transactionsId,
                                         JsonPatch transactionsPatch,
                                         Authentication authentication) {
        try {
            String username = authentication.getName();
            User sessionUser = userService.getUser(username); //Finds session user

            Account account = accountService.getAccountByName(sessionUser, accountName); //Finds account

            Transaction transactionDB = transactionService.getTransactionById(transactionsId, account.getIdAccount()); //Finds transaction

            Transaction patchedTransaction = jsonPatcher //Patches the transaction
                    .applyPatchToObject(transactionsPatch, transactionDB);


            return ResponseEntity.ok(transactionService.update(transactionDB, patchedTransaction).toString()); //Saves the updated transaction

        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Object> getTransactions(String accountName,
                                                  Date from, Date to,
                                                  int page, int size,
                                                  Authentication authentication) {

        if (size > 1000) {
            return ResponseEntity.badRequest().body("Size max limit is 1000, cant be higher");
        }

        if (from == null && to == null) {
            from = new Date(Instant.now().toEpochMilli() - (15 * 1000 * 60 * 60 * 24));
            to = new Date(Instant.now().toEpochMilli() + (15 * 1000 * 60 * 60 * 24));
        }

        if (from == null) {
            from = new Date(Instant.EPOCH.toEpochMilli());
        }
        if (to == null) {
            to = new Date(0x20C5708A70D380L); // 294276 AD
        }

        if (to.before(from)) {
            return ResponseEntity.badRequest().body("'from' Date cant be older than 'to' Date");
        }

        if (Objects.equals(accountName, "*")) {
            return getTransactionsFromAllAccounts(from, to, page, size, authentication);
        } else {
            return getTransactionFromOneAccount(accountName, from, to, page, size, authentication);
        }
    }


    private ResponseEntity<Object> getTransactionsFromAllAccounts(Date from, Date to,
                                                                  int page, int size,
                                                                  Authentication authentication) {

        String username = authentication.getName();
        User sessionUser = userService.getUser(username);

        List<Account> accounts = accountService.getAccountsFromUser(sessionUser);

        List<Object> responseBody = new ArrayList<>();

        for (Account account : accounts) {

            Page<Transaction> transactions =
                    transactionService.getTransactionsFromTo(account.getIdAccount(), from, to, page - 1, size);

            responseBody.add(responseBuilder.buildGetTransactionResponse(transactions, account, from, to, page));

        }

        return ResponseEntity.ok(responseBody);

    }

    private ResponseEntity<Object> getTransactionFromOneAccount(String accountName,
                                                                Date from, Date to,
                                                                int page, int size,
                                                                Authentication authentication) {

        String username = authentication.getName();
        User sessionUser = userService.getUser(username);

        Account account = accountService.getAccountByName(sessionUser, accountName);

        Page<Transaction> transactions =
                transactionService.getTransactionsFromTo(account.getIdAccount(), from, to, page - 1, size);


        return ResponseEntity.ok(responseBuilder.buildGetTransactionResponse(transactions, account, from, to, page));

    }

}
