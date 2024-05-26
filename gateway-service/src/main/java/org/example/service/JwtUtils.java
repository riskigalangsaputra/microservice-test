package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void initKey() {
        // Ensure the secret key is at least 32 characters long (256 bits)
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long");
        }

        try {
            // Convert the secret key to a byte array
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            // Initialize the key using Keys.hmacShaKeyFor
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

    public boolean isExpired(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            throw new RuntimeException("Failed to determine if JWT is expired: " + e.getMessage(), e);
        }
    }
}
