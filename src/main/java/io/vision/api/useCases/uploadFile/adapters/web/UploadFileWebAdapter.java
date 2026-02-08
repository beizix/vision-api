package io.vision.api.useCases.uploadFile.adapters.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.vision.api.useCases.uploadFile.application.UploadFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "파일 업로드", description = "파일 업로드 API")
@RestController
@RequiredArgsConstructor
public class UploadFileWebAdapter {

  private final UploadFileUseCase uploadFileUseCase;
}
