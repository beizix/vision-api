package io.vision.api.useCases.signup.adapters.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.signup.adapters.web.model.SignupManagerReq;
import io.vision.api.useCases.signup.adapters.web.model.SignupRes;
import io.vision.api.useCases.signup.application.SignupUseCase;
import io.vision.api.useCases.signup.application.model.SignupCmd;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Signup API", description = "회원 가입 API")
public class SignupManagerWebAdapter {

  private final SignupUseCase signupUseCase;

  public SignupManagerWebAdapter(SignupUseCase signupUseCase) {
    this.signupUseCase = signupUseCase;
  }

  @Operation(summary = "관리자 가입", description = "새로운 관리자를 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "가입 성공")
  })
  @PostMapping("/api/v1/manager/signup")
  public SignupRes signupManager(
      @RequestBody @Parameter(description = "관리자 가입 정보", required = true) SignupManagerReq req) {
    AuthToken token = signupUseCase.operate(
        new SignupCmd(req.email(), req.password(), req.displayName(), Role.MANAGER));
    return new SignupRes(token.accessToken(), token.refreshToken());
  }
}
