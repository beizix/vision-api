package io.dough.api.useCases.user.profile.updatePassword.adapters.web;

import io.dough.api.useCases.user.profile.updatePassword.adapters.web.model.UpdatePasswordRequest;
import io.dough.api.useCases.user.profile.updatePassword.application.UpdatePasswordUseCase;
import io.dough.api.useCases.user.profile.updatePassword.application.domain.model.UpdatePasswordCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Profile", description = "프로필 관리 API")
class UpdatePasswordWebAdapter {

  private final UpdatePasswordUseCase updatePasswordUseCase;

  @PatchMapping({"/api/v1/user/profile/password", "/api/v1/manager/profile/password"})
  @Operation(
      summary = "사용자 패스워드 변경",
      description = "현재 패스워드를 확인하고 신규 패스워드로 변경합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 (패스워드 불일치 등)", content = @Content(schema = @Schema(implementation = Void.class)))
      }
  )
  public void updatePassword(
      @RequestBody UpdatePasswordRequest request,
      @Parameter(hidden = true) Principal principal) {
    updatePasswordUseCase.operate(new UpdatePasswordCmd(
        UUID.fromString(principal.getName()),
        request.currentPassword(),
        request.newPassword(),
        request.newPasswordConfirm()
    ));
  }
}
