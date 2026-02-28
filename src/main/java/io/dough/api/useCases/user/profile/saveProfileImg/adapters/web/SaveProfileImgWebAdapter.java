package io.dough.api.useCases.user.profile.saveProfileImg.adapters.web;

import io.dough.api.useCases.user.profile.saveProfileImg.adapters.web.model.SaveProfileImgResponse;
import io.dough.api.useCases.user.profile.saveProfileImg.application.SaveProfileImgUseCase;
import io.dough.api.useCases.user.profile.saveProfileImg.application.domain.model.SaveProfileImgCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Profile", description = "사용자 프로필 관련 API")
@RestController
@RequestMapping({"/api/v1/user/profile/img", "/api/v1/manager/profile/img"})
@RequiredArgsConstructor
public class SaveProfileImgWebAdapter {

  private final SaveProfileImgUseCase saveProfileImgUseCase;

  @Operation(summary = "프로필 이미지 저장", description = "사용자의 새로운 프로필 이미지를 업로드하고 저장합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 업로드 실패 등)")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<SaveProfileImgResponse> saveMyProfileImg(
      Principal principal,
      @Parameter(description = "업로드할 프로필 이미지 파일") @RequestParam("file") MultipartFile file)
      throws IOException {

    UUID userId = UUID.fromString(principal.getName());
    SaveProfileImgCmd cmd =
        new SaveProfileImgCmd(
            userId, file.getInputStream(), file.getOriginalFilename(), file.getSize());

    return saveProfileImgUseCase
        .operate(cmd)
        .map(
            result ->
                new SaveProfileImgResponse(
                    result.id(),
                    result.name(),
                    result.originName(),
                    result.fileLength(),
                    result.referURL()))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }
}
