package com.walletAPI.model.service;

import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.User;
import com.walletAPI.model.repositories.AccountRepository;
import com.walletAPI.model.service.exceptions.AccountException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account create(Account account) {
        //Checks if user doesn't have an account with the same name already
        if (accountRepository.findAccountByUserIdUserAndName(account.getUser().getIdUser(), account.getName()).isPresent()) {
            throw new AccountException("{ \"errorCode\": \"ACCOUNT.SERVICE.NAME_DUPLICATED\", \"message\": \"User already has an account with that name\"}");
        }
        return accountRepository.save(account);
    }

    @Override
    public void delete(Account account) {
        accountRepository.findAccountByUserIdUserAndName(account.getUser().getIdUser(), account.getName())
                .ifPresent(value -> accountRepository.deleteAccountByIdAccount(value.getIdAccount()));
        throw new AccountException("{ \"errorCode\": \"ACCOUNT.SERVICE.NOT_FOUND\", \"message\": \"Account not found\"}");
    }

    @Override
    public Account update(Account oldAccount, Account newAccount) {
        //Checks if user doesn't have an account with the same name already
        if (accountRepository.findAccountByUserIdUserAndName(oldAccount.getUser().getIdUser(), newAccount.getName())
                .isPresent()) {
            throw new AccountException("{ \"errorCode\": \"ACCOUNT.SERVICE.NAME_DUPLICATED\", \"message\": \"User already has an account with that name\"}");
        }
        oldAccount.setName(newAccount.getName());

        return accountRepository.save(oldAccount);

    }

    @Override
    public List<Account> getAccountsFromUser(User user) {
        return accountRepository.getAllByUserIdUser(user.getIdUser());
    }

    @Override
    public Account getAccountByName(User user, String accountName) {
        return accountRepository.findAccountByUserIdUserAndName(user.getIdUser(), accountName)
                .orElseThrow(() -> new AccountException("{ \"errorCode\": \"ACCOUNT.NOT_FOUND\", \"message\": \"Account not found\"}"));

    }
}
