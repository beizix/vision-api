package io.dough.api.useCases.user.profile.getProfile.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.getProfile.application.LoadProfile;
import io.dough.api.useCases.user.profile.getProfile.application.domain.model.ProfileLoaded;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoadProfilePersistAdapter implements LoadProfile {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public ProfileLoaded operate(UUID userId) {
    UserEntity entity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new ProfileLoaded(
        entity.getId(),
        entity.getEmail(),
        entity.getDisplayName(),
        entity.getRole(),
        entity.getCreatedAt(),
        entity.getProfileImage() != null ? entity.getProfileImage().getId() : null);
  }
}
