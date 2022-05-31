package com.walletAPI.security.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/refresh")
public interface JWTRefreshController {

    @GetMapping
    ResponseEntity<Object> refresh(@RequestHeader("Refresh-Token") String refresh_token,
                                   HttpServletRequest request);


}
