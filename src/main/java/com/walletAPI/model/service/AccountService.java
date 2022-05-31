package com.walletAPI.model.service;

import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.User;

import java.util.List;


public interface AccountService {

    public Account create(Account account);

    public void delete(Account account);

    public Account update(Account oldAccount, Account newAccount);

    public List<Account> getAccountsFromUser(User user);

    public Account getAccountByName(User user, String accountName);

}
