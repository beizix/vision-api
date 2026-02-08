package io.vision.api.useCases.uploadFile.application;

import io.vision.api.useCases.uploadFile.application.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadFileService implements UploadFileUseCase {
  private final Set<SaveToFileStoragePortOut> fileUploadStrategies;
  private final SaveFileMetadataPortOut saveFileMetadataPortOut;
  private final Tika tika;

  @Override
  public Optional<UploadFile> operate(FileUploadType fileUploadType, MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      return Optional.empty();
    }

    try {
      validateFile(fileUploadType, multipartFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());
    String subPath = getDirPath(fileUploadType.getSubPath());
    String createFilename = getUUIDFilename(getFileExtension(originalFilename).orElse(null));

    try (InputStream inputStream = multipartFile.getInputStream()) {
      getFileUploadStrategy(fileUploadType.getFileStorageType())
          .operate(inputStream, subPath, createFilename);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    SaveFileMetadata saveFileMetadata =
        saveFileMetadataPortOut
            .operate(
                new SaveFileMetadataCmd(
                    fileUploadType,
                    subPath,
                    createFilename,
                    multipartFile.getOriginalFilename(),
                    multipartFile.getSize()))
            .orElseThrow();

    return Optional.of(
        new UploadFile(
            saveFileMetadata.id(),
            fileUploadType,
            subPath,
            createFilename,
            originalFilename,
            multipartFile.getSize(),
            null));
  }

  private void validateFile(FileUploadType fileUploadType, MultipartFile multipartFile)
      throws IOException {
    String originalFilename = Objects.requireNonNull(multipartFile).getOriginalFilename();

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
    try (InputStream is = multipartFile.getInputStream()) {
      String fileMimeType = tika.detect(is, originalFilename);
      getMimeType(fileType, fileMimeType)
          .orElseThrow(
              () ->
                  new RuntimeException(
                      String.format("'%s' - 허용되지 않는 MIME Type 입니다.", fileMimeType)));
    }
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
    return Path.of(path, now.format(formatter)).normalize().toString();
  }

  private String getUUIDFilename(String extension) {
    return UUID.randomUUID() + "." + extension;
  }

  private SaveToFileStoragePortOut getFileUploadStrategy(FileStorageType fileStorageType) {
    return fileUploadStrategies.stream()
        .filter(
            saveToFileStoragePortOut ->
                saveToFileStoragePortOut.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("No file upload strategy found: %s", fileStorageType.name())));
  }
}
