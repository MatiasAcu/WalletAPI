package com.walletAPI.model.service;

import com.walletAPI.model.entities.User;
import com.walletAPI.model.service.exceptions.UserExceptions;


public interface UserService {

    public User create(User user) throws UserExceptions;

    public void delete(String username);

    public User update(User user) throws UserExceptions;

    public User getUser(String username);


}
