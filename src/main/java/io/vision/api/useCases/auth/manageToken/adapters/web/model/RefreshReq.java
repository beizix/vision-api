package io.vision.api.useCases.auth.manageToken.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 갱신 요청")
public record RefreshReq(@Schema(description = "리프레시 토큰") String refreshToken) {}
