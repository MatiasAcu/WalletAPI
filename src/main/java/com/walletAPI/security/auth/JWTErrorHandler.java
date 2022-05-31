package com.walletAPI.security.auth;

import com.walletAPI.controller.responses.ErrorResponse;
import com.walletAPI.controller.responses.ErrorResponseBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JWTErrorHandler {

    private final ErrorResponseBuilder errorResponseBuilder;

    public JWTErrorHandler(ErrorResponseBuilder errorResponseBuilder) {
        this.errorResponseBuilder = errorResponseBuilder;
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException exception) {

        Map<String, Object> body = new HashMap<>();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(errorResponseBuilder.build("JWT_EXCEPTION", "JWT Token is expired"));

        body.put("errors", errorResponseList);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException exception) {

        Map<String, Object> body = new HashMap<>();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(errorResponseBuilder.build("JWT_EXCEPTION", "JWT Token is invalid"));

        body.put("errors", errorResponseList);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
        Map<String, Object> body = new HashMap<>();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(errorResponseBuilder.build("NOT_AUTHORIZED", exception.getMessage()));
        body.put("errors", errorResponseList);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);

    }

}
