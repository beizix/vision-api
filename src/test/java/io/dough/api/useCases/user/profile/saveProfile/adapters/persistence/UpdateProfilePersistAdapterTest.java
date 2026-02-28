package io.dough.api.useCases.user.profile.saveProfile.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.common.application.enums.Role;
import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SavedProfile;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(UpdateProfilePersistAdapter.class)
class UpdateProfilePersistAdapterTest extends DataJpaTestBase {

  @Autowired private UpdateProfilePersistAdapter adapter;
  @Autowired private UserRepository userRepository;
  @Autowired private EntityManager entityManager;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 프로필 정보가 성공적으로 업데이트된다")
  void update_user_profile_success() {
    // Given
    UserEntity existingUser =
        userRepository.save(
            new UserEntity("old.email@example.com", "password", "Old Name", Role.USER, "token"));
    
    // Explicitly flush to ensure createdAt is persisted and not null before update
    entityManager.flush();
    entityManager.clear(); // Detach entity to ensure it's reloaded from DB

    SaveProfileCmd cmd =
        new SaveProfileCmd(existingUser.getId(), "new.email@example.com", "New Name");

    // When
    SavedProfile result = adapter.operate(cmd);
    entityManager.flush(); // Flush to ensure updatedAt is persisted
    entityManager.clear(); // Detach entity

    // Then
    UserEntity updatedUser = userRepository.findById(existingUser.getId()).orElseThrow();
    assertThat(result).isNotNull();
    assertThat(result.email()).isEqualTo("new.email@example.com");
    assertThat(result.displayName()).isEqualTo("New Name");
    assertThat(result.updatedAt()).isNotNull();

    assertThat(updatedUser.getEmail()).isEqualTo("new.email@example.com");
    assertThat(updatedUser.getDisplayName()).isEqualTo("New Name");
    assertThat(updatedUser.getUpdatedAt()).isNotNull();
    assertThat(updatedUser.getUpdatedAt()).isAfter(existingUser.getUpdatedAt());
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 사용자의 프로필 업데이트 시 예외가 발생한다")
  void update_non_existent_user_throws_exception() {
    // Given
    SaveProfileCmd cmd = new SaveProfileCmd(UUID.randomUUID(), "email@example.com", "Name");

    // When
    Throwable thrown = catchThrowable(() -> adapter.operate(cmd));

    // Then
    assertThat(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("User not found for update");
  }
}
