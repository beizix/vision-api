package io.dough.api.useCases.user.getUser.adapters.web;

import io.dough.api.useCases.user.getUser.adapters.web.model.GetUserResponse;
import io.dough.api.useCases.user.getUser.application.GetUserUseCase;
import io.dough.api.useCases.user.getUser.application.domain.model.GetUserCmd;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 조회", description = "사용자 상세 정보 조회 API")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class GetUserWebAdapter {

  private final GetUserUseCase getUserUseCase;

  @Operation(summary = "사용자 상세 정보 조회", description = "사용자 ID를 기반으로 상세 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @GetMapping("/{id}")
  public ResponseEntity<GetUserResponse> getUser(
      @Parameter(description = "사용자 ID", required = true) @PathVariable UUID id) {
    GetUserCmd cmd = new GetUserCmd(id);
    UserDetail result = getUserUseCase.operate(cmd);

    GetUserResponse response = new GetUserResponse(
        result.id(),
        result.email(),
        result.displayName(),
        result.role());

    return ResponseEntity.ok(response);
  }
}
