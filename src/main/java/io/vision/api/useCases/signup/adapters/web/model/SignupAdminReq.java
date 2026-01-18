package io.vision.api.useCases.signup.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "관리자 회원가입 요청")
public record SignupAdminReq(
    @Schema(description = "이메일", example = "admin@vision.io") String email,
    @Schema(description = "비밀번호", example = "password123") String password,
    @Schema(description = "이름", example = "관리자") String displayName) {}
