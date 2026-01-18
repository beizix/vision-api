package io.vision.api.useCases.login.adapters.persistence;

import io.vision.api.common.adapters.persistence.repository.UserRepository;
import io.vision.api.useCases.login.application.LoginPortOut;
import io.vision.api.useCases.login.application.model.LoginUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginPersistAdapter implements LoginPortOut {

  private final UserRepository userRepository;

  @Override
  public Optional<LoginUser> loadUser(String email) {
    return userRepository
        .findByEmail(email)
        .map(
            entity ->
                new LoginUser(
                    entity.getId(),
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getDisplayName(),
                    entity.getRole()));
  }
}
