package io.dough.api.useCases.user.profile.saveProfileImg.application.domain.model;

import java.util.UUID;

public record SavedProfileImg(
    UUID id, String name, String originName, Long fileLength, String referURL) {}
