package io.vision.api.common.application.enums;

import java.util.Set;

public enum Role {
  USER(Set.of(Privilege.ACCESS_USER_API)),
  MANAGER(Set.of(Privilege.ACCESS_MANAGER_API));

  private final Set<Privilege> privileges;

  public String getAuthority() {
    return "ROLE_" + this.name();
  }

  Role(Set<Privilege> privileges) {
    this.privileges = privileges;
  }

  public Set<Privilege> getPrivileges() {
    return privileges;
  }
}