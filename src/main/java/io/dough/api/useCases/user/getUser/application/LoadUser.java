package io.dough.api.useCases.user.getUser.application;

import io.dough.api.useCases.user.getUser.application.domain.model.UserLoaded;
import java.util.UUID;

public interface LoadUser {
  UserLoaded operate(UUID userId);
}
