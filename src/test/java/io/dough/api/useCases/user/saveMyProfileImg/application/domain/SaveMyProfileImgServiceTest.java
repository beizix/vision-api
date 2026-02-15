package io.dough.api.useCases.user.saveMyProfileImg.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.file.saveFile.application.SaveFileUseCase;
import io.dough.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import io.dough.api.useCases.file.saveFile.application.domain.model.SaveFile;
import io.dough.api.useCases.user.saveMyProfileImg.application.UpdateUserProfileImg;
import io.dough.api.useCases.user.saveMyProfileImg.application.domain.model.SaveMyProfileImgCmd;
import io.dough.api.useCases.user.saveMyProfileImg.application.domain.model.SavedProfileImg;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveMyProfileImgServiceTest {

  @Mock private SaveFileUseCase saveFileUseCase;
  @Mock private UpdateUserProfileImg updateUserProfileImg;
  @Mock private GetFileURLUseCase getFileURLUseCase;

  @InjectMocks private SaveMyProfileImgService saveMyProfileImgService;

  @Test
  @DisplayName("Scenario: 성공 - 파일을 저장하고 사용자 정보 업데이트 및 참조 URL을 조회하여 반환한다")
  void save_my_profile_img_service_success() {
    // Given
    UUID userId = UUID.randomUUID();
    byte[] content = "test content".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
    SaveMyProfileImgCmd cmd =
        new SaveMyProfileImgCmd(userId, inputStream, "profile.png", (long) content.length);

    UUID savedFileId = UUID.randomUUID();
    SaveFile mockSaveFile =
        new SaveFile(
            savedFileId,
            FileUploadType.MY_PROFILE_IMG,
            "/user/profile/img",
            "unique_profile.png",
            "profile.png",
            (long) content.length);

    String expectedUrl = "http://example.com/files/" + savedFileId;

    given(
            saveFileUseCase.operate(
                eq(FileUploadType.MY_PROFILE_IMG), any(), eq("profile.png"), eq((long) content.length)))
        .willReturn(Optional.of(mockSaveFile));
    given(getFileURLUseCase.operate(savedFileId)).willReturn(expectedUrl);

    // When
    Optional<SavedProfileImg> result = saveMyProfileImgService.operate(cmd);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().referURL()).isEqualTo(expectedUrl);
    verify(saveFileUseCase)
        .operate(
            eq(FileUploadType.MY_PROFILE_IMG), any(), eq("profile.png"), eq((long) content.length));
    verify(updateUserProfileImg).operate(userId, savedFileId);
    verify(getFileURLUseCase).operate(savedFileId);
  }
}
