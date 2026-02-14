package io.dough.api.useCases.auth.manageToken.application.domain;

import io.dough.api.common.application.utils.MessageUtils;
import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.dough.api.useCases.auth.manageToken.application.RefreshAuthToken;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.dough.api.useCases.auth.manageToken.application.domain.model.RefreshTokenCmd;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ManageAuthTokenService implements ManageAuthTokenUseCase {

  private final SecretKey key;
  private final long accessTokenValidity;
  private final long refreshTokenValidity;
  private final RefreshAuthToken refreshAuthToken;
  private final MessageUtils messageUtils;

  public ManageAuthTokenService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-validity}") long accessTokenValidity,
      @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
      RefreshAuthToken refreshAuthToken,
      MessageUtils messageUtils) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidity = accessTokenValidity;
    this.refreshTokenValidity = refreshTokenValidity;
    this.refreshAuthToken = refreshAuthToken;
    this.messageUtils = messageUtils;
  }

  @Override
  public AuthToken createToken(CreateTokenCmd cmd) {
    var role = cmd.role().getAuthority();
    var privileges = cmd.role().getPrivileges().stream().map(Enum::name).distinct().toList();
    String accessToken =
        createToken(
            cmd.uuid().toString(),
            cmd.email(),
            cmd.displayName(),
            role,
            privileges,
            accessTokenValidity);
    String refreshToken =
        createToken(
            cmd.uuid().toString(),
            cmd.email(),
            cmd.displayName(),
            role,
            privileges,
            refreshTokenValidity);

    refreshAuthToken.save(cmd.uuid(), refreshToken);

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

      return refreshAuthToken
          .get(cmd.refreshToken())
          .map(
              refreshUser ->
                  createToken(
                      new CreateTokenCmd(
                          refreshUser.uuid(),
                          refreshUser.email(),
                          refreshUser.displayName(),
                          refreshUser.role())))
          .orElseThrow(
              () ->
                  new IllegalArgumentException(
                      messageUtils.getMessage("exception.auth.invalid_refresh_token")));
    } catch (Exception e) {
      throw new IllegalArgumentException(
          messageUtils.getMessage("exception.auth.invalid_refresh_token"), e);
    }
  }

  @Override
  public String getSubject(String token) {
    try {
      return parseClaims(token).getSubject();
    } catch (Exception e) {
      throw new IllegalArgumentException(
          messageUtils.getMessage("exception.auth.invalid_token"), e);
    }
  }

  @Override
  public String getDisplayName(String token) {
    try {
      Claims claims = parseClaims(token);
      return claims.get("displayName", String.class);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          messageUtils.getMessage("exception.auth.invalid_token"), e);
    }
  }

  @Override
  public String getEmail(String token) {
    try {
      Claims claims = parseClaims(token);
      return claims.get("email", String.class);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          messageUtils.getMessage("exception.auth.invalid_token"), e);
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
      String email,
      String displayName,
      String role,
      java.util.List<String> privileges,
      long validity) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validity);

    return Jwts.builder()
        .subject(subject)
        .claim("email", email)
        .claim("displayName", displayName)
        .claim("role", role)
        .claim("privileges", privileges)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(key)
        .compact();
  }
}
