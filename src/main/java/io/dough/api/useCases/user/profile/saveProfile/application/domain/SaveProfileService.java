package io.dough.api.useCases.user.profile.saveProfile.application.domain;

import io.dough.api.useCases.user.profile.saveProfile.application.SaveProfileUseCase;
import io.dough.api.useCases.user.profile.saveProfile.application.UpdateProfile;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SavedProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveProfileService implements SaveProfileUseCase {

  private final UpdateProfile updateProfile;

  @Override
  public SavedProfile operate(SaveProfileCmd cmd) {
    return updateProfile.operate(cmd);
  }
}
