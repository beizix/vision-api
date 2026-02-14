package io.dough.api.useCases.user.getUser.application.domain;

import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.user.getUser.application.GetUserUseCase;
import io.dough.api.useCases.user.getUser.application.LoadUser;
import io.dough.api.useCases.user.getUser.application.domain.model.GetUserCmd;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import io.dough.api.useCases.user.getUser.application.domain.model.UserLoaded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserUseCase {

  private final LoadUser loadUser;
  private final GetFileURLUseCase getFileURLUseCase;

  @Override
  public UserDetail operate(GetUserCmd cmd) {
    UserLoaded loaded = loadUser.operate(cmd.id());

    String profileImageUrl =
      loaded.profileImageId() != null ? getFileURLUseCase.operate(loaded.profileImageId()) : null;

    return new UserDetail(
      loaded.id(),
      loaded.email(),
      loaded.displayName(),
      loaded.role(),
      loaded.createdAt(),
      profileImageUrl);
  }
}
