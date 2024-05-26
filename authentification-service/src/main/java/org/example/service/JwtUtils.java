package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private Key key;

    @PostConstruct
    public void initKey() {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long");
        }

        try {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (WeakKeyException e) {
            throw new IllegalArgumentException("The provided JWT secret key is not strong enough. Please ensure it is at least 32 characters long.", e);
        }
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT claims: " + e.getMessage(), e);
        }
    }

    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    private boolean isExpired(String token) {
        try {
            return getExpiration(token).before(new Date());
        } catch (Exception e) {
            throw new RuntimeException("Failed to determine if JWT is expired: " + e.getMessage(), e);
        }
    }

    public String generateToken(String userId, String role, String tokenType) {
        Map<String, String> claims = Map.of("id", userId, "role", role);

        long expirationTime;
        try {
            expirationTime = Long.parseLong(expiration) * 1000;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid JWT expiration time format: " + expiration, e);
        }

        if ("ACCESS".equalsIgnoreCase(tokenType)) {
            expirationTime *= 1;
        } else {
            expirationTime *= 5; // Example of extending expiration for non-ACCESS tokens
        }

        final Date now = new Date();
        final Date exp = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.get("id"))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
