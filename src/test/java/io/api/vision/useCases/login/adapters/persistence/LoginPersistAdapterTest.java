package io.api.vision.useCases.login.adapters.persistence;

import io.api.vision.common.adapters.persistence.entity.UserEntity;
import io.api.vision.common.adapters.persistence.repository.UserRepository;
import io.api.vision.support.DataJpaTestBase;
import io.api.vision.useCases.login.application.model.LoginUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import({LoginPersistAdapter.class})
class LoginPersistAdapterTest extends DataJpaTestBase {

    @Autowired
    private LoginPersistAdapter loginPersistAdapter;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Scenario: 성공 - 이메일로 사용자를 조회하여 LoginUser 모델로 반환한다")
    void load_user_success() {
        // Given
        String email = "persist@test.com";
        userRepository.save(new UserEntity(email, "password", "ROLE_USER"));

        // When
        Optional<LoginUser> result = loginPersistAdapter.loadUser(email);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo(email);
        assertThat(result.get().id()).isNotNull();
        assertThat(result.get().role()).isEqualTo("ROLE_USER");
    }
}
