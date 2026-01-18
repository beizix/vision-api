package io.vision.api.useCases.login.application.model;

import io.vision.api.common.application.enums.Role;
import java.util.UUID;

public record LoginUser(UUID id, String email, String password, String displayName, Role role) {}
