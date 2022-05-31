package com.walletAPI.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTExceptionFilter extends OncePerRequestFilter {

    private final JWTErrorHandler jwtErrorHandler;

    private final ObjectMapper objectMapper;

    public JWTExceptionFilter(JWTErrorHandler jwtErrorHandler, ObjectMapper objectMapper) {
        this.jwtErrorHandler = jwtErrorHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
            res.getWriter().write(objectMapper.writeValueAsString(jwtErrorHandler.handleExpiredJwtException(e).getBody()));
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setStatus(401);
        } catch (JwtException e) {
            res.getWriter().write(objectMapper.writeValueAsString(jwtErrorHandler.handleJwtException(e).getBody()));
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setStatus(401);
        }
    }


}
