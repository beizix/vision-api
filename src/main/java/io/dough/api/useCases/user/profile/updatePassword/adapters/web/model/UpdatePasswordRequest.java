package io.dough.api.useCases.user.profile.updatePassword.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "패스워드 변경 요청")
public record UpdatePasswordRequest(
    @Schema(description = "현재 패스워드", example = "currentPassword123!")
    String currentPassword,
    @Schema(description = "신규 패스워드", example = "newPassword123!")
    String newPassword,
    @Schema(description = "신규 패스워드 확인", example = "newPassword123!")
    String newPasswordConfirm
) {}
