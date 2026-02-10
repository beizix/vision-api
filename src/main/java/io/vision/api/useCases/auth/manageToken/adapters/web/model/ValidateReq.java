package io.vision.api.useCases.auth.manageToken.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 검증 요청")
public record ValidateReq(@Schema(description = "검증할 액세스 토큰") String token) {}
