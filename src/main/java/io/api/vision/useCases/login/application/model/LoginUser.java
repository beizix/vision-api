package io.api.vision.useCases.login.application.model;

import java.util.UUID;

public record LoginUser(UUID id, String email, String password, String role) {
}
