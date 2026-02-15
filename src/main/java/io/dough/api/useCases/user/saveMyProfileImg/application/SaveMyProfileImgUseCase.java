package io.dough.api.useCases.user.saveMyProfileImg.application;

import io.dough.api.useCases.user.saveMyProfileImg.application.domain.model.SaveMyProfileImgCmd;
import io.dough.api.useCases.user.saveMyProfileImg.application.domain.model.SavedProfileImg;
import java.util.Optional;

public interface SaveMyProfileImgUseCase {
  Optional<SavedProfileImg> operate(SaveMyProfileImgCmd cmd);
}
