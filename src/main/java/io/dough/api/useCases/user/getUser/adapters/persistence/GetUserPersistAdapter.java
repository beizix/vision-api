package io.dough.api.useCases.user.getUser.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.getUser.application.LoadUser;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserPersistAdapter implements LoadUser {

  private final UserRepository userRepository;

  @Override
  public UserDetail operate(UUID userId) {
    UserEntity entity = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new UserDetail(
        entity.getId(),
        entity.getEmail(),
        entity.getDisplayName(),
        entity.getRole());
  }
}
