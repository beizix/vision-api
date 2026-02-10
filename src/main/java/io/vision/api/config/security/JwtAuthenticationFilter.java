package io.vision.api.config.security;

import io.vision.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final ManageAuthTokenUseCase manageAuthTokenUseCase;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Optional<String> tokenOptional = resolveToken(request);

    tokenOptional
        .filter(manageAuthTokenUseCase::validateToken)
        .ifPresent(
            token -> {
              String email = manageAuthTokenUseCase.getSubject(token);
              List<SimpleGrantedAuthority> authorities = Stream.concat(
                      Stream.of(manageAuthTokenUseCase.getRole(token)),
                      manageAuthTokenUseCase.getPrivileges(token).stream()
                  )
                  .filter(Objects::nonNull)
                  .map(SimpleGrantedAuthority::new)
                  .toList();

              UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null,
                  authorities);
              SecurityContextHolder.getContext().setAuthentication(authentication);
            });

    filterChain.doFilter(request, response);
  }

  private Optional<String> resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return Optional.of(bearerToken.substring(7));
    }
    return Optional.empty();
  }
}
