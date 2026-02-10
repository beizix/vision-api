package io.vision.api.useCases.auth.login.application.domain;

import io.vision.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.vision.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.login.application.LoginUseCase;
import io.vision.api.useCases.auth.login.application.domain.model.LoginCmd;
import io.vision.api.useCases.auth.login.application.domain.model.GetUser;
import io.vision.api.useCases.auth.login.application.ports.GetUserPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

  private final GetUserPortOut getUserPortOut;
  private final ManageAuthTokenUseCase manageAuthTokenUseCase;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthToken operate(LoginCmd cmd) {
    GetUser user = getUserPortOut
        .operate(cmd.email())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!passwordEncoder.matches(cmd.password(), user.password())) {
      throw new IllegalArgumentException("Invalid password");
    }

    return manageAuthTokenUseCase.createToken(
        new CreateTokenCmd(user.email(), user.displayName(), user.role()));
  }
}
