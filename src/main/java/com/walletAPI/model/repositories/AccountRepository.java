package com.walletAPI.model.repositories;

import com.walletAPI.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Optional<Account> findAccountByUserIdUserAndName(Long userId, String name);

    @Modifying
    public void deleteAccountByIdAccount(Long idAccount);

    public List<Account> getAllByUserIdUser(Long userId);

}
