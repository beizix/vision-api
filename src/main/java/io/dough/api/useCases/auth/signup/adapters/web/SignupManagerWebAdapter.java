package io.dough.api.useCases.auth.signup.adapters.web;

import io.dough.api.common.application.enums.Role;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.signup.adapters.web.model.SignupManagerRequest;
import io.dough.api.useCases.auth.signup.adapters.web.model.SignupResponse;
import io.dough.api.useCases.auth.signup.application.SignupUseCase;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "가입 성공")})
  @PostMapping("/api/v1/manager/signup")
  public SignupResponse signupManager(
      @RequestBody @Parameter(description = "관리자 가입 정보", required = true)
          SignupManagerRequest req) {
    AuthToken token =
        signupUseCase.operate(
            new SignupCmd(req.email(), req.password(), req.displayName(), Role.MANAGER));
    return new SignupResponse(token.accessToken(), token.refreshToken());
  }
}
