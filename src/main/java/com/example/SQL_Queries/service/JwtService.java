package com.example.SQL_Queries.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${app.secret-key}")
    String secretKey;
    Key key;

    @PostConstruct
    public void initKey(){
        byte [] bites = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bites);
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 100 * 1000))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    public boolean isExpired(String token){
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }
    public boolean isTokenValid(String token, String userLogged){
        try {
            String usernameToken = extractUsername(token);
            return (userLogged.equals(usernameToken) && !isExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}
