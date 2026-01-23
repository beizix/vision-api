package io.vision.api.useCases.login.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.vision.api.common.adapters.persistence.entity.UserEntity;
import io.vision.api.common.adapters.persistence.repository.UserRepository;
import io.vision.api.common.application.enums.Role;
import io.vision.api.support.DataJpaTestBase;
import io.vision.api.useCases.login.application.model.LoginUser;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ LoginPersistAdapter.class })
class LoginPersistAdapterTest extends DataJpaTestBase {

  @Autowired
  private LoginPersistAdapter loginPersistAdapter;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 이메일로 사용자 정보를 로드한다")
  void load_user_success() {
    // Given
    String email = "persist@example.com";
    userRepository.save(new UserEntity(email, "password", "Persist User", Role.USER, null));

    // When
    Optional<LoginUser> result = loginPersistAdapter.loadUser(email);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().email()).isEqualTo(email);
    assertThat(result.get().id()).isNotNull();
    assertThat(result.get().role()).isEqualTo(Role.USER);
  }
}
