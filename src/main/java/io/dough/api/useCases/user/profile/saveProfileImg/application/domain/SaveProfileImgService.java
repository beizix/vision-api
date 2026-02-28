package io.dough.api.useCases.user.profile.saveProfileImg.application.domain;

import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.file.saveFile.application.SaveFileUseCase;
import io.dough.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import io.dough.api.useCases.user.profile.saveProfileImg.application.SaveProfileImgUseCase;
import io.dough.api.useCases.user.profile.saveProfileImg.application.UpdateUserProfileImg;
import io.dough.api.useCases.user.profile.saveProfileImg.application.domain.model.SaveProfileImgCmd;
import io.dough.api.useCases.user.profile.saveProfileImg.application.domain.model.SavedProfileImg;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveProfileImgService implements SaveProfileImgUseCase {

  private final SaveFileUseCase saveFileUseCase;
  private final UpdateUserProfileImg updateUserProfileImg;
  private final GetFileURLUseCase getFileURLUseCase;

  @Override
  public Optional<SavedProfileImg> operate(SaveProfileImgCmd cmd) {
    return saveFileUseCase
        .operate(
            FileUploadType.MY_PROFILE_IMG,
            cmd.inputStream(),
            cmd.originalFilename(),
            cmd.fileSize())
        .map(
            file -> {
              updateUserProfileImg.operate(cmd.userId(), file.id());
              String referURL = getFileURLUseCase.operate(file.id());

              return new SavedProfileImg(
                  file.id(), file.name(), file.originName(), file.fileLength(), referURL);
            });
  }
}
