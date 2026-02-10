package io.vision.api.useCases.auth.login.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginReq(
    @Schema(description = "사용자 이메일") String email,
    @Schema(description = "사용자 비밀번호") String password) {}
