package io.dough.api.useCases.user.profile.saveProfile.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.saveProfile.application.UpdateProfile;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SavedProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateProfilePersistAdapter implements UpdateProfile {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public SavedProfile operate(SaveProfileCmd cmd) {
    UserEntity userEntity =
        userRepository
            .findById(cmd.loginUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found for update"));

    userEntity.setEmail(cmd.email());
    userEntity.setDisplayName(cmd.displayName());

    UserEntity savedUser = userRepository.save(userEntity);

    return new SavedProfile(
        savedUser.getEmail(), savedUser.getDisplayName(), savedUser.getUpdatedAt());
  }
}
