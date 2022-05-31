package com.walletAPI.controller;


import com.github.fge.jsonpatch.JsonPatch;
import com.walletAPI.controller.requests.TransactionRequestInterface;
import com.walletAPI.model.entities.Transaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.walletAPI.controller.AccountController.ACCOUNT_URL;
import static com.walletAPI.controller.TransactionController.TRANSACTION_URL;


@RequestMapping(TRANSACTION_URL)
public interface TransactionController {

    String TRANSACTION_URL = ACCOUNT_URL + "/{name}/transactions";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> create(@Validated(TransactionRequestInterface.class)
                                  @RequestBody Transaction transaction,
                                  @PathVariable("name") String accountName,
                                  Authentication authentication);

    @DeleteMapping(value = "{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<Object> delete(@PathVariable("name") String accountName,
                                  @PathVariable("id") Long transactionsId,
                                  Authentication authentication);

    @PatchMapping(value = "{id}", consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> update(@PathVariable("name") String accountName,
                                  @PathVariable("id") Long transactionsId,
                                  @RequestBody JsonPatch transactionPatch,
                                  Authentication authentication);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getTransactions(@PathVariable("name") String accountName,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to,
                                           @RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false, defaultValue = "50") int size,
                                           Authentication authentication);


}
