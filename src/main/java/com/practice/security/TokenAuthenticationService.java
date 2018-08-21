package com.practice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationService {

    private String secret = "mySuperSecretToken";

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().get("username",String.class);
    }

    public String getPasswordFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().get("password",String.class);
    }

    public String createToken(String username, String password) {
        return Jwts.builder()
                .claim("username",username)
                .claim("password",password)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
