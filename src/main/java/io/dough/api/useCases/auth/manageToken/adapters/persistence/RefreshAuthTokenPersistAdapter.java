package io.dough.api.useCases.auth.manageToken.adapters.persistence;

import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.auth.manageToken.application.RefreshAuthToken;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RefreshAuthTokenPersistAdapter implements RefreshAuthToken {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<RefreshUser> get(String refreshToken) {
    return userRepository
        .findByRefreshToken(refreshToken)
        .map(
            entity ->
                new RefreshUser(
                    entity.getId(), entity.getEmail(), entity.getDisplayName(), entity.getRole()));
  }

  @Override
  @Transactional
  public void save(UUID uuid, String refreshToken) {
    userRepository
        .findById(uuid)
        .ifPresent(
            entity -> {
              // UserEntity에 setter가 있으므로 이를 활용합니다.
              // 도메인 규칙상 생성자 패턴을 권장하지만, JPA 엔티티의 특정 필드 업데이트는
              // 영속성 컨텍스트의 변경 감지를 활용하기 위해 setter를 사용하거나 별도 메서드를 둡니다.
              entity.setRefreshToken(refreshToken);
            });
  }
}
