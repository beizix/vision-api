package io.vision.api.useCases.login.application;

import io.vision.api.useCases.auth.application.JwtUseCase;
import io.vision.api.useCases.auth.application.model.AuthCmd;
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
  private final JwtUseCase jwtUseCase;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthToken operate(LoginCmd cmd) {
    LoginUser user =
        loginPortOut
            .loadUser(cmd.email())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!passwordEncoder.matches(cmd.password(), user.password())) {
      throw new IllegalArgumentException("Invalid password");
    }

    return jwtUseCase.createToken(
        new AuthCmd(user.email(), user.displayName(), java.util.Collections.singletonList(user.role())));
  }
}
