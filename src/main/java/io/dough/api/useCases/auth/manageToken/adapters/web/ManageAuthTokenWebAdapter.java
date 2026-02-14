package io.dough.api.useCases.auth.manageToken.adapters.web;

import io.dough.api.useCases.auth.manageToken.adapters.web.model.RefreshRequest;
import io.dough.api.useCases.auth.manageToken.adapters.web.model.RefreshResponse;
import io.dough.api.useCases.auth.manageToken.adapters.web.model.ValidateRequest;
import io.dough.api.useCases.auth.manageToken.adapters.web.model.ValidateResponse;
import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.manageToken.application.domain.model.RefreshTokenCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ManageAuthTokenWebAdapter {

  private final ManageAuthTokenUseCase manageAuthTokenUseCase;

  @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.")
  @ApiResponse(responseCode = "200", description = "토큰 갱신 성공")
  @PostMapping("/refresh")
  public RefreshResponse refresh(
      @RequestBody @Parameter(description = "토큰 갱신 요청", required = true) RefreshRequest req) {
    AuthToken token = manageAuthTokenUseCase.refreshToken(new RefreshTokenCmd(req.refreshToken()));
    return new RefreshResponse(token.accessToken(), token.refreshToken());
  }

  @Operation(summary = "토큰 검증", description = "액세스 토큰의 유효성을 검증합니다.")
  @ApiResponse(responseCode = "200", description = "토큰 검증 결과 반환")
  @PostMapping("/validate")
  public ValidateResponse validate(
      @RequestBody @Parameter(description = "토큰 검증 요청", required = true) ValidateRequest req) {
    boolean isValid = manageAuthTokenUseCase.validateToken(req.token());
    return new ValidateResponse(isValid);
  }
}
