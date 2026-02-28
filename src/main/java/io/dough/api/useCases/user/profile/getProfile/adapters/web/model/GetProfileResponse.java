package io.dough.api.useCases.user.profile.getProfile.adapters.web.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetProfileResponse(
    UUID id, String email, String displayName, LocalDateTime createdAt, String profileImageUrl) {}
