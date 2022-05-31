package com.walletAPI.security.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.walletAPI.security.auth.SecurityConstants.*;

@RestController
public class JWTRefreshControllerImpl implements JWTRefreshController {

    private final JWTErrorHandler jwtErrorHandler;

    private final JWTService jwtService;

    public JWTRefreshControllerImpl(JWTService jwtService, JWTErrorHandler jwtErrorHandler) {
        this.jwtService = jwtService;
        this.jwtErrorHandler = jwtErrorHandler;
    }

    @Override
    public ResponseEntity<Object> refresh(String refresh_token, HttpServletRequest request) {

        try {

            Claims credentials = jwtService.parse(refresh_token);

            String[] tokens = jwtService.generateTokens(credentials.getSubject());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER_AUTHORIZATION_KEY, TOKEN_BEARER_PREFIX + " " + tokens[0]);
            headers.add(HEADER_REFRESH_TOKEN_KEY, TOKEN_BEARER_PREFIX + " " + tokens[1]);
            headers.add(HEADER_EXPIRATION_DATE, tokens[2]);
            return ResponseEntity.noContent().headers(headers).build();

        } catch (ExpiredJwtException e) {
            return jwtErrorHandler.handleExpiredJwtException(e);
        } catch (JwtException e) {
            return jwtErrorHandler.handleJwtException(e);
        }
    }

}

