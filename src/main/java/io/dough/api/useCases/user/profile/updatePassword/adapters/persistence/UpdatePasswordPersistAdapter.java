package io.dough.api.useCases.user.profile.updatePassword.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.updatePassword.application.GetUser;
import io.dough.api.useCases.user.profile.updatePassword.application.SaveUser;
import io.dough.api.useCases.user.profile.updatePassword.application.domain.model.UpdatePassword;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UpdatePasswordPersistAdapter implements GetUser, SaveUser {

  private final UserRepository userRepository;

  @Override
  public UpdatePassword operate(UUID userId) {
    UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
    return new UpdatePassword(userEntity.getId(), userEntity.getPassword());
  }

  @Override
  public void operate(UpdatePassword updatePassword) {
    UserEntity userEntity = userRepository.findById(updatePassword.id())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
    userEntity.setPassword(updatePassword.encodedPassword());
    userRepository.save(userEntity);
  }
}
