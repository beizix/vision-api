package io.dough.api.useCases.user.profile.updatePassword.application.domain.model;

import java.util.UUID;

public record UpdatePasswordCmd(
    UUID userId,
    String currentPassword,
    String newPassword,
    String newPasswordConfirm
) {}
