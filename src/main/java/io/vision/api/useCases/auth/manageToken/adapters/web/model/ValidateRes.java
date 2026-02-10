package io.vision.api.useCases.auth.manageToken.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 검증 응답")
public record ValidateRes(@Schema(description = "토큰 유효 여부") boolean valid) {}
