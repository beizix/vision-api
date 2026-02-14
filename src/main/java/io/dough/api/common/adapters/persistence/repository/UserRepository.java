package io.dough.api.common.adapters.persistence.repository;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.application.enums.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmailAndRole(String email, Role role);

  boolean existsByEmailAndRole(String email, Role role);

  Optional<UserEntity> findByRefreshToken(String refreshToken);
}
