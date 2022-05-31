package com.walletAPI.controller.responses;

import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.User;

import java.util.List;
import java.util.Map;

public interface AccountResponseBuilder {

    /* This methods are used to build the HTTP responses for the Account Controller */

    Map<String, Object> buildCreateAccountResponse(User user, Account account);

    Map<String, Object> buildGetAccountsResponse(User sessionUser, List<Account> accounts);
}
