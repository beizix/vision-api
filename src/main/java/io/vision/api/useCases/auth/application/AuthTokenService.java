package io.vision.api.useCases.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.auth.application.model.CreateTokenCmd;
import io.vision.api.useCases.auth.application.model.RefreshTokenCmd;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthTokenService implements AuthTokenUseCase {

  private final SecretKey key;
  private final long accessTokenValidity;
  private final long refreshTokenValidity;
  private final RefreshTokenPortOut refreshTokenPortOut;

  public AuthTokenService(
      @Value("${jwt.secret:v-api-secret-key-for-jwt-token-generation-2026}") String secret,
      @Value("${jwt.access-token-validity:3600000}") long accessTokenValidity,
      @Value("${jwt.refresh-token-validity:86400000}") long refreshTokenValidity,
      RefreshTokenPortOut refreshTokenPortOut) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidity = accessTokenValidity;
    this.refreshTokenValidity = refreshTokenValidity;
    this.refreshTokenPortOut = refreshTokenPortOut;
  }

  @Override
  public AuthToken createToken(CreateTokenCmd cmd) {
    var role = cmd.role().getAuthority();
    var privileges = cmd.role().getPrivileges().stream()
        .map(Enum::name)
        .distinct()
        .toList();
    String accessToken = createToken(cmd.email(), cmd.displayName(), role, privileges, accessTokenValidity);
    String refreshToken = createToken(cmd.email(), cmd.displayName(), role, privileges, refreshTokenValidity);

    refreshTokenPortOut.save(cmd.email(), refreshToken);

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
      validateToken(cmd.refreshToken());

      return refreshTokenPortOut.get(cmd.refreshToken())
          .map(refreshUser -> createToken(new CreateTokenCmd(refreshUser.email(), refreshUser.displayName(), refreshUser.role())))
          .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
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

  @Override
  public String getDisplayName(String token) {
    try {
      Claims claims = parseClaims(token);
      return claims.get("displayName", String.class);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid token", e);
    }
  }

  @Override
  public String getRole(String token) {
    try {
      Claims claims = parseClaims(token);
      return claims.get("role", String.class);
    } catch (Exception e) {
      log.error("Failed to extract role from token", e);
      return null;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public java.util.List<String> getPrivileges(String token) {
    try {
      Claims claims = parseClaims(token);
      return claims.get("privileges", java.util.List.class);
    } catch (Exception e) {
      log.error("Failed to extract privileges from token", e);
      return java.util.Collections.emptyList();
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  private String createToken(
      String subject,
      String displayName,
      String role,
      java.util.List<String> privileges,
      long validity) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validity);

    return Jwts.builder()
        .subject(subject)
        .claim("displayName", displayName)
        .claim("role", role)
        .claim("privileges", privileges)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(key)
        .compact();
  }
}
