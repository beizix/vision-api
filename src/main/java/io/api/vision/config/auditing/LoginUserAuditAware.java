package io.api.vision.config.auditing;

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

    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.of("SYSTEM"); // 인증되지 않은 경우 "SYSTEM" 반환
    }

    // 실제 애플리케이션에서는 UserDetails 객체에서 사용자 ID를 추출해야 합니다.
    // 여기서는 간단하게 principal의 이름을 반환합니다.
    return Optional.of(authentication.getName());
  }
}
