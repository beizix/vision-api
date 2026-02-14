package io.dough.api.useCases.auth.signup.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupManagerRequest(
    @Schema(description = "이메일", example = "manager@dough.io") String email,
    @Schema(description = "비밀번호", example = "password") String password,
    @Schema(description = "이름", example = "Manager User") String displayName) {}
