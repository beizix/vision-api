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
@Table(name = "users", comment = "사용자 테이블")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "deleted = false")
public class UserEntity extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(comment = "사용자 UUID")
  private UUID id;

  @Column(unique = true, nullable = false, comment = "사용자 이메일")
  private String email;

  @Column(nullable = false, comment = "사용자 패스워드")
  private String password;

  @Column(nullable = false, comment = "사용자 표기명")
  private String displayName;

  @Column(nullable = false, length = 255, comment = "사용자 역할")
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(comment = "사용자 refresh 토큰")
  private String refreshToken;

  public UserEntity(String email, String password, String displayName, Role role, String refreshToken) {
    this.email = email;
    this.password = password;
    this.displayName = displayName;
    this.role = role;
    this.refreshToken = refreshToken;
  }
}
