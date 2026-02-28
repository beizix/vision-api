package io.dough.api.useCases.user.profile.saveProfile.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 프로필 저장 요청")
public record SaveProfileRequest(
    @Schema(description = "새 이메일 주소", example = "new.user@example.com") String email,
    @Schema(description = "새 표시 이름", example = "NewDisplayName") String displayName) {}
