package io.vision.api.useCases.auth.signup.application.domain;

import io.vision.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.vision.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.signup.application.ports.SignupPortOut;
import io.vision.api.useCases.auth.signup.application.SignupUseCase;
import io.vision.api.useCases.auth.signup.application.domain.model.SignupCmd;
import io.vision.api.useCases.auth.signup.application.domain.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

  private final SignupPortOut signupPortOut;
  private final PasswordEncoder passwordEncoder;
  private final ManageAuthTokenUseCase manageAuthTokenUseCase;

  @Override
  @Transactional
  public AuthToken operate(SignupCmd cmd) {
    if (signupPortOut.existsByEmailAndRole(cmd.email(), cmd.role())) {
      throw new IllegalArgumentException("이미 해당 권한으로 가입된 이메일입니다.");
    }

    String encodedPassword = passwordEncoder.encode(cmd.password());
    SignupUser user = new SignupUser(cmd.email(), encodedPassword, cmd.displayName(), cmd.role());

    signupPortOut.save(user);

    return manageAuthTokenUseCase.createToken(
        new CreateTokenCmd(user.email(), user.displayName(), user.role()));
  }
}
