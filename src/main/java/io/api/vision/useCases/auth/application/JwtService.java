package io.api.vision.useCases.auth.application;

import io.api.vision.useCases.auth.application.model.AuthCmd;
import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.auth.application.model.RefreshTokenCmd;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JwtService implements JwtUseCase {

    private final SecretKey key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtService(
            @Value("${jwt.secret:v-api-secret-key-for-jwt-token-generation-2026}") String secret,
            @Value("${jwt.access-token-validity:3600000}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity:86400000}") long refreshTokenValidity) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    @Override
    public AuthToken createToken(AuthCmd cmd) {
        String accessToken = createToken(cmd.email(), accessTokenValidity);
        String refreshToken = createToken(cmd.email(), refreshTokenValidity);
        return new AuthToken(accessToken, refreshToken);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public AuthToken refreshToken(RefreshTokenCmd cmd) {
        try {
            Claims claims = parseClaims(cmd.refreshToken());
            String email = claims.getSubject();
            return createToken(new AuthCmd(email));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid refresh token", e);
        }
    }

    @Override
    public String getSubject(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String createToken(String subject, long validity) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }
}
