package io.dough.api.useCases.user.getMyProfile.application.domain;

import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.user.getMyProfile.application.GetMyProfileUseCase;
import io.dough.api.useCases.user.getMyProfile.application.LoadMyProfile;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.GetMyProfileCmd;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.MyProfile;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.MyProfileLoaded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyProfileService implements GetMyProfileUseCase {

  private final LoadMyProfile loadMyProfile;
  private final GetFileURLUseCase getFileURLUseCase;

  @Override
  public MyProfile operate(GetMyProfileCmd cmd) {
    MyProfileLoaded loaded = loadMyProfile.operate(cmd.id());

    String profileImageUrl =
      loaded.profileImageId() != null ? getFileURLUseCase.operate(loaded.profileImageId()) : null;

    return new MyProfile(
      loaded.id(),
      loaded.email(),
      loaded.displayName(),
      loaded.createdAt(),
      loaded.profileImageId(),
      profileImageUrl);
  }
}
