package io.vision.api.useCases.auth.login.adapters.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.login.adapters.web.model.LoginReq;
import io.vision.api.useCases.auth.login.adapters.web.model.LoginRes;
import io.vision.api.useCases.auth.login.application.LoginUseCase;
import io.vision.api.useCases.auth.login.application.domain.model.LoginCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
public class LoginWebAdapter {

  private final LoginUseCase loginUseCase;

  @Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  @PostMapping("/api/v1/auth/login")
  public LoginRes login(
      @RequestBody @Parameter(description = "로그인 요청", required = true) LoginReq req) {
    AuthToken token = loginUseCase.operate(new LoginCmd(req.email(), req.password()));
    return new LoginRes(token.accessToken(), token.refreshToken());
  }
}
