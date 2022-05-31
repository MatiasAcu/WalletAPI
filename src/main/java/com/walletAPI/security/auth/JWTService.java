package com.walletAPI.security.auth;

import io.jsonwebtoken.*;

import java.time.Instant;
import java.util.Date;

import static com.walletAPI.security.auth.SecurityConstants.*;


public class JWTService {


    public JWTService() {
    }

    public Claims parse(String token) throws ExpiredJwtException, JwtException {
        return Jwts.parser()
                .setSigningKey(SUPER_SECRET_KEY)
                .parseClaimsJws(token.replace(TOKEN_BEARER_PREFIX, ""))
                .getBody();
    }

    public String[] generateTokens(String username) {

        long current_time = Instant.now().toEpochMilli();

        String token = Jwts.builder().setIssuedAt(new Date()).setIssuer(ISSUER_INFO)
                .setSubject(username)
                .setExpiration(new Date(current_time + TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();

        String refresh_token = Jwts.builder().setIssuedAt(new Date()).setIssuer(ISSUER_INFO)
                .setSubject(username)
                .setExpiration(new Date(current_time + (TOKEN_EXPIRATION_TIME * 2)))
                .signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();

        String expiration_date = String.valueOf(current_time + TOKEN_EXPIRATION_TIME);

        return new String[]{token, refresh_token, expiration_date};


    }


}
