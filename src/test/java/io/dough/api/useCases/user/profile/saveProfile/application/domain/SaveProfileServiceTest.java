package io.dough.api.useCases.user.profile.saveProfile.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.user.profile.saveProfile.application.SaveProfileUseCase;
import io.dough.api.useCases.user.profile.saveProfile.application.UpdateProfile;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SavedProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveProfileServiceTest {

  @Mock private UpdateProfile updateProfile;

  @InjectMocks private SaveProfileService saveProfileService;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 프로필이 성공적으로 업데이트된다")
  void save_user_profile_success() {
    // Given
    UUID userId = UUID.randomUUID();
    String newEmail = "new.user@example.com";
    String newDisplayName = "New User Name";
    SaveProfileCmd cmd = new SaveProfileCmd(userId, newEmail, newDisplayName);
    LocalDateTime now = LocalDateTime.now();
    SavedProfile expectedProfile = new SavedProfile(newEmail, newDisplayName, now);

    given(updateProfile.operate(any(SaveProfileCmd.class))).willReturn(expectedProfile);

    // When
    SavedProfile result = saveProfileService.operate(cmd);

    // Then
    assertThat(result).isEqualTo(expectedProfile);
    verify(updateProfile).operate(cmd);
  }
}
