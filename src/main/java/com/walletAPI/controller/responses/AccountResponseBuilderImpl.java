package com.walletAPI.controller.responses;

import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountResponseBuilderImpl implements AccountResponseBuilder {


    public AccountResponseBuilderImpl() {
    }

    @Override
    public Map<String, Object> buildCreateAccountResponse(User user, Account account) {

        Map<String, Object> body = new HashMap<>();
        body.put("username", user.getUsername());
        body.put("email", user.getEmail());
        body.put("accounts", account);

        return body;
    }

    @Override
    public Map<String, Object> buildGetAccountsResponse(User sessionUser, List<Account> accounts) {

        Map<String, Object> body = new HashMap<>();

        body.put("username", sessionUser.getUsername());
        body.put("email", sessionUser.getEmail());
        body.put("accounts", accounts);

        return body;

    }
}
