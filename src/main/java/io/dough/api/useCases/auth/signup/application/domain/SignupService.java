package io.dough.api.useCases.auth.signup.application.domain;

import io.dough.api.common.application.utils.MessageUtils;
import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.dough.api.useCases.auth.signup.application.ManageSignup;
import io.dough.api.useCases.auth.signup.application.SignupUseCase;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupCmd;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

  private final ManageSignup manageSignup;
  private final PasswordEncoder passwordEncoder;
  private final ManageAuthTokenUseCase manageAuthTokenUseCase;
  private final MessageUtils messageUtils;

  @Override
  @Transactional
  public AuthToken operate(SignupCmd cmd) {
    if (manageSignup.existsByEmailAndRole(cmd.email(), cmd.role())) {
      throw new IllegalArgumentException(
          messageUtils.getMessage("exception.auth.email_already_exists"));
    }

    String encodedPassword = passwordEncoder.encode(cmd.password());
    SignupUser user =
        new SignupUser(null, cmd.email(), encodedPassword, cmd.displayName(), cmd.role());

    SignupUser savedUser = manageSignup.save(user);

    return manageAuthTokenUseCase.createToken(
        new CreateTokenCmd(
            savedUser.id(), savedUser.email(), savedUser.displayName(), savedUser.role()));
  }
}
