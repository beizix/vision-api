package io.vision.api.useCases.auth.login.application.domain;

import io.vision.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.vision.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.login.application.LoginUseCase;
import io.vision.api.useCases.auth.login.application.domain.model.LoginCmd;
import io.vision.api.useCases.auth.login.application.domain.model.GetUserResult;
import io.vision.api.useCases.auth.login.application.GetUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

  private final GetUser getUser;
  private final ManageAuthTokenUseCase manageAuthTokenUseCase;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthToken operate(LoginCmd cmd) {
    GetUserResult user = getUser
        .operate(cmd.email())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!passwordEncoder.matches(cmd.password(), user.password())) {
      throw new IllegalArgumentException("Invalid password");
    }

    return manageAuthTokenUseCase.createToken(
        new CreateTokenCmd(user.email(), user.displayName(), user.role()));
  }
}
