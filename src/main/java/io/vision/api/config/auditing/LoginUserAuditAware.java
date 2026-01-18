package io.vision.api.config.auditing;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginUserAuditAware implements AuditorAware<String> {

  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getName())) {
      return Optional.of("SYSTEM"); // 인증되지 않은 경우 "SYSTEM" 반환
    }

    return Optional.of(authentication.getName());
  }
}
