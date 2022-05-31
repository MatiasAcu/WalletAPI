package com.walletAPI.model.service;


import com.walletAPI.model.entities.User;
import com.walletAPI.model.repositories.UserRepository;
import com.walletAPI.model.service.exceptions.UserExceptions;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;


@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(user.getUsername(), user.getPassword(), user.getEmail());
    }

    @Override
    public User create(User user) throws UserExceptions {
        UserExceptions exceptions = checkUserForInsert(user);
        if (exceptions.getExceptions().isEmpty()) {
            return userRepository.save(user);
        }
        throw exceptions;
    }

    @Override
    public void delete(String username) {
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public User update(User user) throws UserExceptions {
        UserExceptions exceptions = checkUserForUpdate(user);
        if (exceptions.getExceptions().isEmpty()) {
            return userRepository.save(user);
        }
        throw exceptions;
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }


    private UserExceptions checkUserForInsert(User user) {
        UserExceptions exceptions = new UserExceptions(new HashSet<>());
        if (userRepository.checkUserByEmailForInsert(user.getEmail()) > 0) {
            exceptions.add(new RuntimeException("{ \"errorCode\": \"USER.SERVICE.DUPLICATED_EMAIL\", \"message\": \"Email already used\"}"));
        }
        if (userRepository.checkUserByUsernameForInsert(user.getUsername()) > 0) {
            exceptions.add(new RuntimeException("{ \"errorCode\": \"USER.SERVICE.DUPLICATED_NAME\", \"message\": \"Username already used\"}"));
        }
        return exceptions;
    }

    private UserExceptions checkUserForUpdate(User user) {

        UserExceptions exceptions = new UserExceptions(new HashSet<>());
        if (userRepository.checkUserByEmailForUpdate(user.getEmail(), user.getIdUser()) > 0) {
            exceptions.add(new RuntimeException("{ \"errorCode\": \"USER.SERVICE.DUPLICATED_EMAIL\", \"message\": \"Email already used\"}"));
        }
        if (userRepository.checkUserByUsernameForUpdate(user.getUsername(), user.getIdUser()) > 0) {
            exceptions.add(new RuntimeException("{ \"errorCode\": \"USER.SERVICE.DUPLICATED_NAME\", \"message\": \"Username already used\"}"));
        }
        return exceptions;
    }

}

