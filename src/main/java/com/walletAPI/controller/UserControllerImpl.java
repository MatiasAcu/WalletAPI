package com.walletAPI.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.walletAPI.json.JsonPatcher;
import com.walletAPI.model.entities.User;
import com.walletAPI.model.service.UserService;
import com.walletAPI.security.auth.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

import static com.walletAPI.security.auth.SecurityConstants.*;


@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JsonPatcher<User> jsonPatcher;

    private final JWTService jwtService;


    public UserControllerImpl(UserService userService, BCryptPasswordEncoder passwordEncoder, JsonPatcher<User> jsonPatcher, JWTService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jsonPatcher = jsonPatcher;
        this.jwtService = jwtService;
    }


    @Override
    public ResponseEntity<String> create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user).toString());
    }


    @Override
    public ResponseEntity<String> delete(Authentication authentication) {
        String username = authentication.getName();
        userService.delete(username);
        return ResponseEntity.ok("User deleted succesfully");
    }


    @Override
    public ResponseEntity<Object> update(JsonPatch userPatch,
                                         Authentication authentication,
                                         HttpServletResponse response) {
        try {
            String username = authentication.getName();
            User userDB = userService.getUser(username); //Finds the user

            User patchedUser = jsonPatcher.applyPatchToObject(userPatch, userDB); //Patches the user
            patchedUser.setIdUser(userDB.getIdUser());

            //If password updated is not encrypted, it encrypts the new password
            Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

            if (!BCRYPT_PATTERN.matcher(patchedUser.getPassword()).matches()) {
                patchedUser.setPassword(passwordEncoder.encode(patchedUser.getPassword()));
            }

            //Builds new token with the updated user credentials

            String[] jwtTokens = jwtService.generateTokens(patchedUser.getUsername());

            response.addHeader(HEADER_AUTHORIZATION_KEY, TOKEN_BEARER_PREFIX + " " + jwtTokens[0]);
            response.addHeader(HEADER_REFRESH_TOKEN_KEY, TOKEN_BEARER_PREFIX + " " + jwtTokens[1]);

            return ResponseEntity.ok(userService.update(patchedUser).toString());

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @Override
    public ResponseEntity<Object> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUser(authentication.getName()).toString());
    }
}


