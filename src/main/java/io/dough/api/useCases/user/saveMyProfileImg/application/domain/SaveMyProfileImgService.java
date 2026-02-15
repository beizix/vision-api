package io.dough.api.useCases.user.saveMyProfileImg.application.domain;

import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.file.saveFile.application.SaveFileUseCase;
import io.dough.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import io.dough.api.useCases.user.saveMyProfileImg.application.SaveMyProfileImgUseCase;
import io.dough.api.useCases.user.saveMyProfileImg.application.UpdateUserProfileImg;
import io.dough.api.useCases.user.saveMyProfileImg.application.domain.model.SaveMyProfileImgCmd;
import io.dough.api.useCases.user.saveMyProfileImg.application.domain.model.SavedProfileImg;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveMyProfileImgService implements SaveMyProfileImgUseCase {

  private final SaveFileUseCase saveFileUseCase;
  private final UpdateUserProfileImg updateUserProfileImg;
  private final GetFileURLUseCase getFileURLUseCase;

  @Override
  public Optional<SavedProfileImg> operate(SaveMyProfileImgCmd cmd) {
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
                  file.id(),
                  file.type(),
                  file.path(),
                  file.name(),
                  file.originName(),
                  file.fileLength(),
                  referURL);
            });
  }
}
