package io.vision.api.common.adapters.persistence.repository;

import io.vision.api.common.adapters.persistence.entity.UserEntity;
import io.vision.api.common.application.enums.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmail(String email);

  boolean existsByEmailAndRole(String email, Role role);
}
