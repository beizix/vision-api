package io.vision.api.common.application.enums;

import java.util.Set;

public enum Role {
  ROLE_USER(Set.of(Privilege.ACCESS_USER_API)),
  ROLE_MANAGER(Set.of(Privilege.ACCESS_MANAGER_API));

  private final Set<Privilege> privileges;

  Role(Set<Privilege> privileges) {
    this.privileges = privileges;
  }

  public Set<Privilege> getPrivileges() {
    return privileges;
  }
}