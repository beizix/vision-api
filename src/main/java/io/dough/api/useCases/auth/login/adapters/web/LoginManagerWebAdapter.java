package io.dough.api.useCases.auth.login.adapters.web;

import io.dough.api.common.application.enums.Role;
import io.dough.api.useCases.auth.login.adapters.web.model.LoginRequest;
import io.dough.api.useCases.auth.login.adapters.web.model.LoginResponse;
import io.dough.api.useCases.auth.login.application.LoginUseCase;
import io.dough.api.useCases.auth.login.application.domain.model.LoginCmd;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
public class LoginManagerWebAdapter {

  private final LoginUseCase loginUseCase;

  @Operation(summary = "매니저 로그인")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  @PostMapping("/api/v1/auth/login/manager")
  public LoginResponse operate(
      @RequestBody @Parameter(description = "로그인 요청", required = true) LoginRequest req) {
    AuthToken token = loginUseCase.operate(new LoginCmd(req.email(), req.password(), Role.MANAGER));
    return new LoginResponse(token.accessToken(), token.refreshToken());
  }
}
