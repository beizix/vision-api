package io.vision.api.useCases.auth.login.adapters.persistence;

import io.vision.api.common.adapters.persistence.repository.UserRepository;
import io.vision.api.useCases.auth.login.application.GetUser;
import io.vision.api.useCases.auth.login.application.domain.model.GetUserResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserPersistAdapter implements GetUser {

  private final UserRepository userRepository;

  @Override
  public Optional<GetUserResult> operate(String email) {
    return userRepository
        .findByEmail(email)
        .map(
            entity ->
                new GetUserResult(
                    entity.getId(),
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getDisplayName(),
                    entity.getRole()));
  }
}
