package io.dough.api.useCases.user.getUser.application;

import io.dough.api.useCases.user.getUser.application.domain.model.GetUserCmd;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;

public interface GetUserUseCase {
  UserDetail operate(GetUserCmd cmd);
}
