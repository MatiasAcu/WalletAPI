package com.walletAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.walletAPI.controller.responses.AccountResponseBuilder;
import com.walletAPI.controller.responses.AccountResponseBuilderImpl;
import com.walletAPI.json.JsonPatcher;
import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.User;
import com.walletAPI.model.service.AccountService;
import com.walletAPI.model.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    private final UserService userService;

    private final JsonPatcher<Account> jsonPatcher;

    private final AccountResponseBuilder accountResponseBuilder;

    public AccountControllerImpl(AccountService accountService, UserService userService, JsonPatcher<Account> jsonPatcher, AccountResponseBuilderImpl accountResponseBuilder) {
        this.accountService = accountService;
        this.userService = userService;
        this.jsonPatcher = jsonPatcher;
        this.accountResponseBuilder = accountResponseBuilder;
    }

    @Override
    public ResponseEntity<Object> create(Account account, Authentication authentication) {
        String username = authentication.getName();
        User sessionUser = userService.getUser(username); //Finds session user
        account.setUser(sessionUser); //Attaches new account to session user

        Account newAccount = accountService.create(account); //Creates account

        return ResponseEntity.created( //Returns URI to access new account and the account data as body
                        URI.create(
                                ACCOUNT_URL + "/" + account.getIdAccount()))
                .body(accountResponseBuilder.buildCreateAccountResponse(sessionUser, newAccount));

    }

    @Override
    public ResponseEntity<Object> delete(String accountName, Authentication authentication) {

        String username = authentication.getName();
        User sessionUser = userService.getUser(username); //Finds session user
        accountService.delete(accountService.getAccountByName(sessionUser, accountName)); //Deletes account
        return ResponseEntity.ok("Account " + accountName + " deleted successfully");

    }

    @Override
    public ResponseEntity<Object> update(String accountName,
                                         JsonPatch accountPatch,
                                         Authentication authentication) {

        try {
            String username = authentication.getName();
            User sessionUser = userService.getUser(username); //Finds session user

            Account accountDB = accountService.getAccountByName(sessionUser, accountName); //Finds account

            Account patchedAccount = jsonPatcher
                    .applyPatchToObject(accountPatch, accountDB); //Patches the account

            return ResponseEntity.ok(accountService.update(accountDB, patchedAccount).toString()); //Saves the updated account

        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Object> getAccounts(Authentication authentication) {

        String username = authentication.getName();
        User sessionUser = userService.getUser(username); //Finds session user
        List<Account> accounts = accountService.getAccountsFromUser(sessionUser); //Gets all the accounts from that user

        return ResponseEntity.ok(accountResponseBuilder.buildGetAccountsResponse(sessionUser, accounts));
    }

}
