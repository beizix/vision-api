package io.dough.api.useCases.user.getUser.application.domain.model;

import io.dough.api.common.application.enums.Role;
import java.util.UUID;

public record UserDetail(
    UUID id,
    String email,
    String displayName,
    Role role) {
}
