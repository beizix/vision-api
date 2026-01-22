package io.vision.api.useCases.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.application.model.CreateTokenCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.auth.application.model.RefreshTokenCmd;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
  public AuthToken createToken(CreateTokenCmd cmd) {
    var roles = cmd.roles().stream().map(Role::getAuthority).toList();
    String accessToken = createToken(cmd.email(), cmd.displayName(), roles, accessTokenValidity);
    String refreshToken = createToken(cmd.email(), cmd.displayName(), roles, refreshTokenValidity);
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
      String displayName = claims.get("displayName", String.class);
      // TODO: 추후 Refresh Token에도 role 정보를 포함할지 결정 필요. 현재는 빈 리스트 전달.
      return createToken(new CreateTokenCmd(email, displayName, java.util.Collections.emptyList()));
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
  @SuppressWarnings("unchecked")
  public java.util.List<String> getRoles(String token) {
    try {
      Claims claims = parseClaims(token);
      return claims.get("roles", java.util.List.class);
    } catch (Exception e) {
      log.error("Failed to extract roles from token", e);
      return java.util.Collections.emptyList();
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  private String createToken(
      String subject, String displayName, java.util.List<String> roles, long validity) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validity);

    return Jwts.builder()
        .subject(subject)
        .claim("displayName", displayName)
        .claim("roles", roles)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(key)
        .compact();
  }
}
