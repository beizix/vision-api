package io.dough.api.useCases.auth.login.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.common.application.enums.Role;
import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.auth.login.application.domain.model.GetUserResult;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({GetUserPersistAdapter.class})
class LoginPersistAdapterTest extends DataJpaTestBase {

  @Autowired private GetUserPersistAdapter loginPersistAdapter;

  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 이메일로 사용자 정보를 로드한다")
  void load_user_success() {
    // Given
    String email = "persist@example.com";
    Role role = Role.USER;
    userRepository.save(new UserEntity(email, "password", "Persist User", role, null));

    // When
    Optional<GetUserResult> result = loginPersistAdapter.operate(email, role);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().email()).isEqualTo(email);
    assertThat(result.get().id()).isNotNull();
    assertThat(result.get().role()).isEqualTo(role);
  }
}
