package io.vision.api.useCases.signup.application;

import io.vision.api.useCases.auth.application.JwtUseCase;
import io.vision.api.useCases.auth.application.model.AuthCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.signup.application.model.SignupCmd;
import io.vision.api.useCases.signup.application.model.SignupUser;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

  private final SignupPortOut signupPortOut;
  private final PasswordEncoder passwordEncoder;
  private final JwtUseCase jwtUseCase;

  @Override
  @Transactional
  public AuthToken operate(SignupCmd cmd) {
    if (signupPortOut.existsByEmailAndRole(cmd.email(), cmd.role())) {
      throw new IllegalArgumentException("이미 해당 권한으로 가입된 이메일입니다.");
    }

    String encodedPassword = passwordEncoder.encode(cmd.password());
    SignupUser user = new SignupUser(cmd.email(), encodedPassword, cmd.displayName(), cmd.role());

    signupPortOut.save(user);

    return jwtUseCase.createToken(
        new AuthCmd(user.email(), Collections.singletonList(user.role())));
  }
}
