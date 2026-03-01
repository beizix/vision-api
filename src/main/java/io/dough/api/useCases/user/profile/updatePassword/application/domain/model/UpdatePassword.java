package io.dough.api.useCases.user.profile.updatePassword.application.domain.model;

import java.util.UUID;

public record UpdatePassword(
    UUID id,
    String encodedPassword
) {}
