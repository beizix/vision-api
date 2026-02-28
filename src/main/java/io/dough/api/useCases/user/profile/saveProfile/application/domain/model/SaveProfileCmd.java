package io.dough.api.useCases.user.profile.saveProfile.application.domain.model;

import java.util.UUID;

public record SaveProfileCmd(UUID loginUserId, String email, String displayName) {}
