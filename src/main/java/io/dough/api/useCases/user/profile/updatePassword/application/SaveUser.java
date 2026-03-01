package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.application.domain.model.UpdatePassword;

public interface SaveUser {
  void operate(UpdatePassword updatePassword);
}
