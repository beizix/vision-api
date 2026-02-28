package io.dough.api.useCases.user.profile.saveProfile.application;

import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SavedProfile;

public interface SaveProfileUseCase {
  SavedProfile operate(SaveProfileCmd cmd);
}
