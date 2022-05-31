package com.walletAPI.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletAPI.controller.responses.ErrorResponse;
import com.walletAPI.model.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.walletAPI.security.auth.SecurityConstants.*;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final ObjectMapper objectMapper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User credentials = new ObjectMapper().readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(), credentials.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {


        String[] tokens = jwtService.generateTokens(((User) auth.getPrincipal()).getUsername());

        response.addHeader(HEADER_AUTHORIZATION_KEY, TOKEN_BEARER_PREFIX + " " + tokens[0]);
        response.addHeader(HEADER_REFRESH_TOKEN_KEY, TOKEN_BEARER_PREFIX + " " + tokens[1]);
        response.addHeader(HEADER_EXPIRATION_DATE, tokens[2]);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<>();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(new ErrorResponse("NOT_AUTHORIZED", failed.getMessage()));
        body.put("errors", errorResponseList);

        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.setContentType("application/json");
        response.setStatus(401);

    }
}
