package com.walletAPI.model.repositories;

import com.walletAPI.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.email= :email and u.idUser <> :idUser ")
    public Long checkUserByEmailForUpdate(String email, Long idUser);

    @Query("SELECT COUNT(u) FROM User u WHERE u.username= :username and u.idUser <> :idUser")
    public Long checkUserByUsernameForUpdate(String username, Long idUser);

    @Query("SELECT COUNT(u) FROM User u WHERE u.email= :email")
    public Long checkUserByEmailForInsert(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.username= :username")
    public Long checkUserByUsernameForInsert(String username);

    @Modifying
    public void deleteUserByUsername(String username);
}
