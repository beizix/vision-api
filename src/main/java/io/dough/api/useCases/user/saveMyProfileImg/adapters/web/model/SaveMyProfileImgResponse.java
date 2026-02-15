package io.dough.api.useCases.user.saveMyProfileImg.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "프로필 이미지 저장 응답")
public record SaveMyProfileImgResponse(
    @Schema(description = "저장된 파일의 고유 식별자 (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,
    @Schema(description = "시스템에 저장된 실제 파일명", example = "unique-profile-name.png")
    String name,
    @Schema(description = "사용자가 업로드한 원본 파일명", example = "profile.png")
    String originName,
    @Schema(description = "파일 크기 (bytes)", example = "1024")
    Long fileLength,
    @Schema(description = "파일에 접근 가능한 참조 URL", example = "http://localhost:8080/files/550e8400-e29b-41d4-a716-446655440000")
    String referURL) {}
