package io.dough.api.useCases.user.getUser.application.domain;

import io.dough.api.useCases.user.getUser.application.GetUserUseCase;
import io.dough.api.useCases.user.getUser.application.LoadUser;
import io.dough.api.useCases.user.getUser.application.domain.model.GetUserCmd;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserUseCase {

  private final LoadUser loadUser;

  @Override
  public UserDetail operate(GetUserCmd cmd) {
    return loadUser.operate(cmd.id());
  }
}
