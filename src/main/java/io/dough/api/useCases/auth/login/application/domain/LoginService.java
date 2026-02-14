package io.dough.api.useCases.auth.login.application.domain;

import io.dough.api.common.application.utils.MessageUtils;
import io.dough.api.useCases.auth.login.application.GetUser;
import io.dough.api.useCases.auth.login.application.LoginUseCase;
import io.dough.api.useCases.auth.login.application.domain.model.GetUserResult;
import io.dough.api.useCases.auth.login.application.domain.model.LoginCmd;
import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

  private final GetUser getUser;
  private final ManageAuthTokenUseCase manageAuthTokenUseCase;
  private final PasswordEncoder passwordEncoder;
  private final MessageUtils messageUtils;

  @Override
  public AuthToken operate(LoginCmd cmd) {
    GetUserResult user =
        getUser
            .operate(cmd.email(), cmd.role())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        messageUtils.getMessage("exception.user.not_found")));

    if (!passwordEncoder.matches(cmd.password(), user.password())) {
      throw new IllegalArgumentException(
          messageUtils.getMessage("exception.auth.invalid_password"));
    }

    return manageAuthTokenUseCase.createToken(
        new CreateTokenCmd(user.id(), user.email(), user.displayName(), user.role()));
  }
}
