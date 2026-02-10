package io.vision.api.useCases.file.saveFile.application.domain;

import io.vision.api.useCases.file.saveFile.application.SaveFileUseCase;
import io.vision.api.useCases.file.saveFile.application.domain.model.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import io.vision.api.useCases.file.saveFile.application.SaveFileMetadata;
import io.vision.api.useCases.file.saveFile.application.SaveToFileStorage;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveFileService implements SaveFileUseCase {
  private final Set<SaveToFileStorage> fileUploadStrategies;
  private final SaveFileMetadata saveFileMetadata;
  private final Tika tika;

  /**
   * Tika를 이용한 파일 타입 감지(detect)는 파일의 앞부분(Magic Bytes)만 읽으므로, 유효성 검사 후 스트림을 초기화(reset)하기 위한 마킹 한계치를
   * 64KB로 설정함.
   */
  private static final int MARK_READ_LIMIT = 64 * 1024;

  @Override
  public Optional<SaveFile> operate(
      FileUploadType fileUploadType,
      InputStream inputStream,
      String originalFilename,
      long fileSize) {
    if (inputStream == null || originalFilename == null || originalFilename.isEmpty()) {
      return Optional.empty();
    }

    try (InputStream bis = new BufferedInputStream(inputStream)) {
      bis.mark(MARK_READ_LIMIT);
      validateFile(fileUploadType, bis, originalFilename);
      bis.reset();

      String subPath = getDirPath(fileUploadType.getSubPath());
      String createFilename = getUUIDFilename(getFileExtension(originalFilename).orElse(null));

      getFileUploadStrategy(fileUploadType.getFileStorageType())
          .operate(bis, subPath, createFilename);

      SaveFileMetadataResult saveFileMetadata =
          this.saveFileMetadata
              .operate(
                  new SaveFileMetadataCmd(
                      fileUploadType, subPath, createFilename, originalFilename, fileSize))
              .orElseThrow();

      return Optional.of(
          new SaveFile(
              saveFileMetadata.id(),
              fileUploadType,
              subPath,
              createFilename,
              originalFilename,
              fileSize));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void validateFile(
      FileUploadType fileUploadType, InputStream inputStream, String originalFilename)
      throws IOException {
    // ✦ 파일 확장자 여부 체크
    String extension =
        getFileExtension(originalFilename)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        String.format("'%s' - 파일 확장자가 존재하지 않습니다.", originalFilename)));

    // ✦ 파일 확장자 체크
    AcceptableFileType fileType =
        getFileTypeMatchingExtension(fileUploadType, extension)
            .orElseThrow(
                () -> new RuntimeException(String.format("'.%s' - 허용되지 않는 파일 확장자입니다.", extension)));

    // ✦ 파일 mime-type 체크
    String fileMimeType = tika.detect(inputStream, originalFilename);
    getMimeType(fileType, fileMimeType)
        .orElseThrow(
            () ->
                new RuntimeException(String.format("'%s' - 허용되지 않는 MIME Type 입니다.", fileMimeType)));
  }

  private Optional<String> getFileExtension(String filename) {
    return Optional.of(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1).toLowerCase());
  }

  private Optional<AcceptableFileType> getFileTypeMatchingExtension(
      FileUploadType fileUploadType, String extension) {
    return fileUploadType.getAcceptableFileTypes().stream()
        .filter(acceptableFileType -> acceptableFileType.getExtensions().contains(extension))
        .findAny();
  }

  private Optional<String> getMimeType(AcceptableFileType acceptableFileType, String fileMimeType) {
    return acceptableFileType.getMimeTypes().stream()
        .filter(mimeType -> mimeType.equals(fileMimeType))
        .findAny();
  }

  private String getDirPath(String path) {
    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    return Path.of(path, now.format(formatter)).normalize().toString().replace("\\", "/");
  }

  private String getUUIDFilename(String extension) {
    return UUID.randomUUID() + "." + extension;
  }

  private SaveToFileStorage getFileUploadStrategy(FileStorageType fileStorageType) {
    return fileUploadStrategies.stream()
        .filter(
          saveToFileStorage ->
                saveToFileStorage.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("No file upload strategy found: %s", fileStorageType.name())));
  }
}
