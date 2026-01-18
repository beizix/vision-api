package io.vision.api.common.adapters.persistence.entity;

import io.vision.api.common.adapters.persistence.component.AuditEntity;
import io.vision.api.common.application.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "deleted = false")
public class UserEntity extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "UUID")
  private UUID id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String displayName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  public UserEntity(String email, String password, String displayName, Role role) {
    this.email = email;
    this.password = password;
    this.displayName = displayName;
    this.role = role;
  }
}
