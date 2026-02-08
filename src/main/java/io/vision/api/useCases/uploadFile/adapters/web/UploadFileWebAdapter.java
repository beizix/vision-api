package io.vision.api.useCases.uploadFile.adapters.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vision.api.useCases.uploadFile.adapters.web.model.Base64MultipartFile;
import io.vision.api.useCases.uploadFile.adapters.web.model.UploadBase64Req;
import io.vision.api.useCases.uploadFile.adapters.web.model.UploadFileRes;
import io.vision.api.useCases.uploadFile.adapters.web.model.UploadMultipartReq;
import io.vision.api.useCases.uploadFile.application.UploadFileUseCase;
import io.vision.api.useCases.uploadFile.application.model.UploadFile;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "파일 업로드", description = "파일 업로드 API")
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadFileWebAdapter {

  private final UploadFileUseCase uploadFileUseCase;

  @Operation(summary = "Multipart 파일 업로드", description = "Multipart/form-data 형식으로 파일을 업로드합니다.")
  @ApiResponse(responseCode = "200", description = "업로드 성공", content = @Content(schema = @Schema(implementation = UploadFileRes.class)))
  @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UploadFileRes uploadMultipart(@Valid @ModelAttribute UploadMultipartReq req) throws IOException {

    UploadFile result = uploadFileUseCase.operate(
            req.type(),
            req.file().getInputStream(),
            req.file().getOriginalFilename(),
            req.file().getSize()
        )
        .orElseThrow(() -> new RuntimeException("파일 업로드에 실패했습니다."));

    return UploadFileRes.from(result);
  }

  @Operation(summary = "Base64 파일 업로드", description = "Base64 인코딩된 문자열 형식으로 파일을 업로드합니다.")
  @ApiResponse(responseCode = "200", description = "업로드 성공", content = @Content(schema = @Schema(implementation = UploadFileRes.class)))
  @PostMapping("/base64Data")
  public UploadFileRes uploadBase64(@Valid @RequestBody UploadBase64Req req) throws IOException {
    Base64MultipartFile file = new Base64MultipartFile(req.base64Data());

    UploadFile result = uploadFileUseCase.operate(
            req.type(),
            file.getInputStream(),
            file.getOriginalFilename(),
            file.getSize()
        )
        .orElseThrow(() -> new RuntimeException("파일 업로드에 실패했습니다."));

    return UploadFileRes.from(result);
  }
}
