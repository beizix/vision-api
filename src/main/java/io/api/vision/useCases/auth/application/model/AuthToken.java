package io.api.vision.useCases.auth.application.model;

public record AuthToken(String accessToken, String refreshToken) {
}
