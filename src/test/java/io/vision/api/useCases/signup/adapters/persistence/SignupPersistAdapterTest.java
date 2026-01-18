package io.vision.api.useCases.signup.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.vision.api.common.adapters.persistence.repository.UserRepository;
import io.vision.api.common.application.enums.Role;
import io.vision.api.support.DataJpaTestBase;
import io.vision.api.useCases.signup.application.model.SignupUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(SignupPersistAdapter.class)
class SignupPersistAdapterTest extends DataJpaTestBase {

  @Autowired private SignupPersistAdapter signupPersistAdapter;

  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - SignupUser 모델을 저장하면 UserEntity로 변환되어 DB에 영속화된다")
  void save_user_success() {
    // Given
    SignupUser user =
        new SignupUser("persist@vision.io", "encodedPassword", "Persist User", Role.ROLE_USER);

    // When
    signupPersistAdapter.save(user);

    // Then
    var foundUser = userRepository.findByEmail(user.email());
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getEmail()).isEqualTo(user.email());
    assertThat(foundUser.get().getDisplayName()).isEqualTo(user.displayName());
    assertThat(foundUser.get().getRole()).isEqualTo(user.role());
  }

  @Test
  @DisplayName("Scenario: 성공 - 존재하는 이메일과 권한 조회 시 true를 반환한다")
  void exists_by_email_and_role_true() {
    // Given
    String email = "exists@vision.io";
    signupPersistAdapter.save(new SignupUser(email, "pass", "User", Role.ROLE_USER));

    // When
    boolean exists = signupPersistAdapter.existsByEmailAndRole(email, Role.ROLE_USER);

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("Scenario: 성공 - 존재하지 않는 이메일 조회 시 false를 반환한다")
  void exists_by_email_false() {
    // When
    boolean exists =
        signupPersistAdapter.existsByEmailAndRole("notfound@vision.io", Role.ROLE_USER);

    // Then
    assertThat(exists).isFalse();
  }
}
