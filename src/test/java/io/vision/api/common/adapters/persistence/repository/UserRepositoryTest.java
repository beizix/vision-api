package io.vision.api.common.adapters.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.vision.api.common.adapters.persistence.entity.UserEntity;
import io.vision.api.common.application.enums.Role;
import io.vision.api.support.DataJpaTestBase;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

class UserRepositoryTest extends DataJpaTestBase {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("Scenario: 성공 - UserEntity 저장 시 Audit 정보가 자동 주입된다")
  void testAuditing_onSave() {
    // Given
    var user = new UserEntity("test@email.com", "password", "Test User 1", Role.USER, null);

    // When
    var savedUser = userRepository.save(user);
    em.flush();
    em.clear();

    // Then
    var foundUser = userRepository.findById(savedUser.getId()).get();
    assertThat(foundUser.getCreatedAt()).isNotNull();
    assertThat(foundUser.getUpdatedAt()).isNotNull();
    assertThat(foundUser.isDeleted()).isFalse();
  }

  @Test
  @DisplayName("Scenario: 성공 - UserEntity 수정 시 updatedAt 정보가 자동 갱신된다")
  void testAuditing_onUpdate() throws InterruptedException {
    // Given
    var savedUser = userRepository.save(
        new UserEntity("test2@email.com", "password", "Test User 2", Role.USER, null));
    em.flush();
    em.clear();

    var reloadedUser = userRepository.findById(savedUser.getId()).get();
    var createdAt = reloadedUser.getCreatedAt();
    Thread.sleep(100); // updatedAt과 시간을 다르게 하기 위해

    // When
    reloadedUser.setPassword("new_password");
    userRepository.saveAndFlush(reloadedUser);
    em.clear();

    // Then
    var foundUser = userRepository.findById(reloadedUser.getId()).get();
    assertThat(foundUser.getCreatedAt()).isEqualTo(createdAt);
    assertThat(foundUser.getUpdatedAt()).isAfter(createdAt);
  }

  @Test
  @DisplayName("Scenario: 성공 - deleted가 true인 User는 조회되지 않는다")
  void testSQLRestriction_onSoftDelete() {
    // Given
    var savedUser = userRepository.save(
        new UserEntity("test3@email.com", "password", "Test User 3", Role.USER, null));
    em.flush();
    em.clear();

    // When
    em.getEntityManager()
        .createQuery("UPDATE UserEntity u SET u.deleted = true WHERE u.id = :id")
        .setParameter("id", savedUser.getId())
        .executeUpdate();
    em.flush();
    em.clear();

    // Then
    Optional<UserEntity> byId = userRepository.findById(savedUser.getId());
    assertThat(byId).isEmpty();
  }

  @Test
  @DisplayName("Scenario: 성공 - refreshToken으로 User를 조회할 수 있다")
  void testFindByRefreshToken() {
    // Given
    var user = new UserEntity("test4@email.com", "password", "Test User 4", Role.USER, "mock-refresh-token");
    userRepository.save(user);
    em.flush();
    em.clear();

    // When
    var foundUser = userRepository.findByRefreshToken("mock-refresh-token");

    // Then
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getEmail()).isEqualTo("test4@email.com");
  }
}
