package io.dough.api.useCases.user.getUser.application;

import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import java.util.UUID;

public interface LoadUser {
  UserDetail operate(UUID userId);
}
