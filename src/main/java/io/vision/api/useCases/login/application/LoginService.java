package io.vision.api.useCases.login.application;

import io.vision.api.useCases.auth.application.AuthTokenUseCase;
import io.vision.api.useCases.auth.application.model.CreateTokenCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.login.application.model.LoginCmd;
import io.vision.api.useCases.login.application.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

  private final LoginPortOut loginPortOut;
  private final AuthTokenUseCase authTokenUseCase;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthToken operate(LoginCmd cmd) {
    LoginUser user = loginPortOut
        .loadUser(cmd.email())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!passwordEncoder.matches(cmd.password(), user.password())) {
      throw new IllegalArgumentException("Invalid password");
    }

    return authTokenUseCase.createToken(
        new CreateTokenCmd(user.email(), user.displayName(), user.role()));
  }
}
