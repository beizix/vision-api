package io.dough.api.useCases.user.getUser.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.getUser.application.LoadUser;
import io.dough.api.useCases.user.getUser.application.domain.model.UserLoaded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoadUserPersistAdapter implements LoadUser {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserLoaded operate(UUID userId) {
    UserEntity entity =
      userRepository
        .findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new UserLoaded(
      entity.getId(),
      entity.getEmail(),
      entity.getDisplayName(),
      entity.getRole(),
      entity.getCreatedAt(),
      entity.getProfileImage() != null ? entity.getProfileImage().getId() : null);
  }
}
