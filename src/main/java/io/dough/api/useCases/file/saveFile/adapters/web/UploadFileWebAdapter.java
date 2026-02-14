package io.dough.api.useCases.file.saveFile.adapters.web;

import io.dough.api.common.application.utils.MessageUtils;
import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.file.saveFile.adapters.web.model.Base64MultipartFile;
import io.dough.api.useCases.file.saveFile.adapters.web.model.UploadBase64Request;
import io.dough.api.useCases.file.saveFile.adapters.web.model.UploadFileResponse;
import io.dough.api.useCases.file.saveFile.adapters.web.model.UploadMultipartRequest;
import io.dough.api.useCases.file.saveFile.application.SaveFileUseCase;
import io.dough.api.useCases.file.saveFile.application.domain.model.SaveFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

  private final SaveFileUseCase saveFileUseCase;
  private final GetFileURLUseCase getFileUrlUseCase;
  private final MessageUtils messageUtils;

  @Operation(summary = "Multipart 파일 업로드", description = "Multipart/form-data 형식으로 파일을 업로드합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "업로드 성공",
      content = @Content(schema = @Schema(implementation = UploadFileResponse.class)))
  @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UploadFileResponse uploadMultipart(@Valid @ModelAttribute UploadMultipartRequest req)
      throws IOException {

    SaveFile result =
        saveFileUseCase
            .operate(
                req.type(),
                req.file().getInputStream(),
                req.file().getOriginalFilename(),
                req.file().getSize())
            .orElseThrow(
                () ->
                    new RuntimeException(messageUtils.getMessage("exception.file.upload_failed")));

    return new UploadFileResponse(
        result.id(),
        result.path(),
        result.name(),
        result.originName(),
        getFileUrlUseCase.operate(result.id()));
  }

  @Operation(summary = "Base64 파일 업로드", description = "Base64 인코딩된 문자열 형식으로 파일을 업로드합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "업로드 성공",
      content = @Content(schema = @Schema(implementation = UploadFileResponse.class)))
  @PostMapping("/base64Data")
  public UploadFileResponse uploadBase64(@Valid @RequestBody UploadBase64Request req)
      throws IOException {
    Base64MultipartFile file = new Base64MultipartFile(req.base64Data());

    SaveFile result =
        saveFileUseCase
            .operate(req.type(), file.getInputStream(), file.getOriginalFilename(), file.getSize())
            .orElseThrow(
                () ->
                    new RuntimeException(messageUtils.getMessage("exception.file.upload_failed")));

    return new UploadFileResponse(
        result.id(),
        result.path(),
        result.name(),
        result.originName(),
        getFileUrlUseCase.operate(result.id()));
  }
}
