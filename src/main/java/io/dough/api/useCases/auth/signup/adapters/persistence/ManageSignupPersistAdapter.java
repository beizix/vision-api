package io.dough.api.useCases.auth.signup.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.common.application.enums.Role;
import io.dough.api.useCases.auth.signup.application.ManageSignup;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManageSignupPersistAdapter implements ManageSignup {

  private final UserRepository userRepository;

  @Override
  public boolean existsByEmailAndRole(String email, Role role) {
    return userRepository.existsByEmailAndRole(email, role);
  }

  @Override
  public SignupUser save(SignupUser user) {
    UserEntity entity =
        new UserEntity(user.email(), user.password(), user.displayName(), user.role(), null);
    UserEntity saved = userRepository.save(entity);
    return new SignupUser(
        saved.getId(),
        saved.getEmail(),
        saved.getPassword(),
        saved.getDisplayName(),
        saved.getRole());
  }
}
