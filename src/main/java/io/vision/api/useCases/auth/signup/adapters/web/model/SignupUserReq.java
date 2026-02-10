package io.vision.api.useCases.auth.signup.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 회원가입 요청")
public record SignupUserReq(
    @Schema(description = "이메일", example = "user@vision.io") String email,
    @Schema(description = "비밀번호", example = "password123") String password,
    @Schema(description = "닉네임", example = "비전매니아") String displayName) {}
