package io.dough.api.useCases.user.getUser.adapters.web.model;

import io.dough.api.common.application.enums.Role;
import java.util.UUID;

public record GetUserResponse(
        UUID id,
        String email,
        String displayName,
        Role role,
        java.time.LocalDateTime createdAt,
        UUID profileImageId) {
}
