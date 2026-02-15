package io.dough.api.useCases.user.getMyProfile.application.domain.model;

import io.dough.api.common.application.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record MyProfile(
    UUID id,
    String email,
    String displayName,
    LocalDateTime createdAt,
    UUID profileImageId,
    String profileImageUrl) {}
