package io.vision.api.useCases.file.saveFile.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vision.api.support.WebMvcTestBase;
import io.vision.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.vision.api.useCases.file.saveFile.application.SaveFileUseCase;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFile;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UploadFileWebAdapter.class)
class SaveFileWebAdapterTest extends WebMvcTestBase {

  @MockitoBean
  private SaveFileUseCase saveFileUseCase;

  @MockitoBean
  private GetFileURLUseCase getFileUrlUseCase;

  @Test
  @DisplayName("Scenario: 성공 - Multipart 파일을 업로드한다")
  void upload_multipart_success() throws Exception {
    // Given
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "test.png",
        MediaType.IMAGE_PNG_VALUE,
        "test content".getBytes()
    );
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UUID fileId = UUID.randomUUID();
    SaveFile saveFile = new SaveFile(fileId, type, "/path/to/file", "saved.png", "test.png", 100L);
    String resourceUrl = "/uploads/path/to/file/saved.png";

    given(saveFileUseCase.operate(eq(type), any(InputStream.class), anyString(), anyLong()))
        .willReturn(Optional.of(saveFile));
    given(getFileUrlUseCase.operate(fileId)).willReturn(resourceUrl);

    // When & Then
    mockMvc.perform(multipart("/api/v1/upload/multipart")
            .file(file)
            .param("type", type.name()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.originName").value("test.png"))
        .andExpect(jsonPath("$.referURL").value(resourceUrl));

    verify(saveFileUseCase).operate(eq(type), any(InputStream.class), anyString(), anyLong());
    verify(getFileUrlUseCase).operate(fileId);
  }

  @Test
  @DisplayName("Scenario: 성공 - Base64 데이터를 업로드한다")
  void upload_base64_success() throws Exception {
    // Given
    String base64Data = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;

    // Enum이 @JsonFormat(Shape.OBJECT)로 설정되어 있어 objectMapper 사용 시 객체로 직렬화됨.
    // 서버의 @RequestBody 역직렬화 호환성을 위해 문자열로 직접 JSON 구성
    String requestBody = """
        {
          "type": "%s",
          "base64Data": "%s"
        }
        """.formatted(type.name(), base64Data);

    UUID fileId = UUID.randomUUID();
    SaveFile saveFile = new SaveFile(fileId, type, "/path/to/file", "saved.png", "image.png", 100L);
    String resourceUrl = "/uploads/path/to/file/saved.png";

    given(saveFileUseCase.operate(eq(type), any(InputStream.class), anyString(), anyLong()))
        .willReturn(Optional.of(saveFile));
    given(getFileUrlUseCase.operate(fileId)).willReturn(resourceUrl);

    // When & Then
    mockMvc.perform(post("/api/v1/upload/base64Data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.referURL").value(resourceUrl));

    verify(saveFileUseCase).operate(eq(type), any(InputStream.class), anyString(), anyLong());
    verify(getFileUrlUseCase).operate(fileId);
  }
}
