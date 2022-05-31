package com.walletAPI.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.walletAPI.model.entities.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.walletAPI.controller.UserController.USER_URL;

@RequestMapping(USER_URL)
public interface UserController {

    String USER_URL = "/users";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> create(@Valid @RequestBody User user);

    @DeleteMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> delete(Authentication authentication);

    @PatchMapping(consumes = "application/json-patch+json", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> update(@RequestBody JsonPatch userPatch,
                                  Authentication authentication,
                                  HttpServletResponse response);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getUser(Authentication authentication);
}
