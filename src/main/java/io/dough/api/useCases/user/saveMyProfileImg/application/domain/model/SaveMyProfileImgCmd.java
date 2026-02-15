package io.dough.api.useCases.user.saveMyProfileImg.application.domain.model;

import java.io.InputStream;
import java.util.UUID;

public record SaveMyProfileImgCmd(
    UUID userId, InputStream inputStream, String originalFilename, long fileSize) {}
