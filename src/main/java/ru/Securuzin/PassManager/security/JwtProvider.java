package ru.Securuzin.PassManager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.SecretKey;
import java.util.Date;


@Component
@RequiredArgsConstructor
@NoArgsConstructor
public class JwtProvider {
    private static final Logger log = LoggerFactory.getLogger( JwtProvider.class);
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Генерирует JWT токен для пользователя
     */
    public String generateToken(String username, Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Вытаскивает username из токена
     */
    public String getUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Вытаскивает userId из токена
     */
    public Long getUserId(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    /**
     * Проверяет валидность токена
     */
    public boolean isTokenValid(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
