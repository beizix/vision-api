package io.vision.api.useCases.auth.signup.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupManagerReq(
    @Schema(description = "이메일", example = "manager@vision.io") String email,
    @Schema(description = "비밀번호", example = "password") String password,
    @Schema(description = "이름", example = "Manager User") String displayName
) {
}
