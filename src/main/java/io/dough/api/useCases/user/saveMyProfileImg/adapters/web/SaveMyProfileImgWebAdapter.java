package io.dough.api.useCases.user.saveMyProfileImg.adapters.web;

import io.dough.api.useCases.user.saveMyProfileImg.adapters.web.model.SaveMyProfileImgResponse;
import io.dough.api.useCases.user.saveMyProfileImg.application.SaveMyProfileImgUseCase;
import io.dough.api.useCases.user.saveMyProfileImg.application.domain.model.SaveMyProfileImgCmd;
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

@Tag(name = "사용자 정보", description = "로그인한 사용자 관련 API")
@RestController
@RequestMapping("/api/v1/user/profile/img")
@RequiredArgsConstructor
public class SaveMyProfileImgWebAdapter {

  private final SaveMyProfileImgUseCase saveMyProfileImgUseCase;

  @Operation(summary = "프로필 이미지 저장", description = "사용자의 새로운 프로필 이미지를 업로드하고 저장합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 업로드 실패 등)")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<SaveMyProfileImgResponse> saveMyProfileImg(
      Principal principal,
      @Parameter(description = "업로드할 프로필 이미지 파일") @RequestParam("file") MultipartFile file)
      throws IOException {

    UUID userId = UUID.fromString(principal.getName());
    SaveMyProfileImgCmd cmd =
        new SaveMyProfileImgCmd(
            userId, file.getInputStream(), file.getOriginalFilename(), file.getSize());

    return saveMyProfileImgUseCase
        .operate(cmd)
        .map(
            result ->
                new SaveMyProfileImgResponse(
                    result.id(),
                    result.name(),
                    result.originName(),
                    result.fileLength(),
                    result.referURL()))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }
}
