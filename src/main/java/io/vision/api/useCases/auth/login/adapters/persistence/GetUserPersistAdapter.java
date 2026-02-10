package io.vision.api.useCases.auth.login.adapters.persistence;

import io.vision.api.common.adapters.persistence.repository.UserRepository;
import io.vision.api.useCases.auth.login.application.ports.GetUserPortOut;
import io.vision.api.useCases.auth.login.application.domain.model.GetUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserPersistAdapter implements GetUserPortOut {

  private final UserRepository userRepository;

  @Override
  public Optional<GetUser> operate(String email) {
    return userRepository
        .findByEmail(email)
        .map(
            entity ->
                new GetUser(
                    entity.getId(),
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getDisplayName(),
                    entity.getRole()));
  }
}
