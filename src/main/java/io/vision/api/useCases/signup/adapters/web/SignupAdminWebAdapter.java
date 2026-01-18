package io.vision.api.useCases.signup.adapters.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.signup.adapters.web.model.SignupAdminReq;
import io.vision.api.useCases.signup.adapters.web.model.SignupRes;
import io.vision.api.useCases.signup.application.SignupUseCase;
import io.vision.api.useCases.signup.application.model.SignupCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입", description = "회원가입 관련 API")
@RestController
@RequiredArgsConstructor
public class SignupAdminWebAdapter {

  private final SignupUseCase signupUseCase;

  @Operation(summary = "관리자 회원가입", description = "관리자 계정을 생성하고 토큰을 발급합니다.")
  @ApiResponse(responseCode = "200", description = "회원가입 성공")
  @PostMapping("/api/v1/signup/admin")
  public SignupRes signupAdmin(
      @RequestBody @Parameter(description = "관리자 가입 정보", required = true) SignupAdminReq req) {
    AuthToken token =
        signupUseCase.operate(
            new SignupCmd(req.email(), req.password(), req.displayName(), Role.ROLE_ADMIN));
    return new SignupRes(token.accessToken(), token.refreshToken());
  }
}
