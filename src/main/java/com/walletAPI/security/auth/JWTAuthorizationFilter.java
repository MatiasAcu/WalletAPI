package com.walletAPI.security.auth;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.walletAPI.security.auth.SecurityConstants.HEADER_AUTHORIZATION_KEY;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    private final JWTService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, JWTService jwtService) {
        super(authManager);
        this.jwtService = jwtService;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(HEADER_AUTHORIZATION_KEY);
        if (header == null) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws ExpiredJwtException, JwtException {
        String token = request.getHeader(HEADER_AUTHORIZATION_KEY);
        if (token != null) {
            String user = jwtService.parse(token).getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}