package com.walletAPI.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.walletAPI.controller.requests.AccountRequestInterface;
import com.walletAPI.model.entities.Account;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.walletAPI.controller.AccountController.ACCOUNT_URL;
import static com.walletAPI.controller.UserController.USER_URL;


@RequestMapping(ACCOUNT_URL)
public interface AccountController {

    String ACCOUNT_URL = USER_URL + "/accounts";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> create(@Validated(AccountRequestInterface.class)
                                  @RequestBody Account account, Authentication authentication);


    @DeleteMapping(value = "/{name}", produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<Object> delete(@PathVariable(value = "name") String accountName,
                                  Authentication authentication);


    @PatchMapping(value = "/{name}",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> update(@PathVariable("name") String accountName,
                                  @RequestBody JsonPatch accountPatch,
                                  Authentication authentication);


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getAccounts(Authentication authentication);


}
