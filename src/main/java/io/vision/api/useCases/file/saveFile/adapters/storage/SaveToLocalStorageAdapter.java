package io.vision.api.useCases.file.saveFile.adapters.storage;

import io.vision.api.useCases.file.saveFile.application.ports.SaveToFileStoragePortOut;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveToLocalStorageAdapter implements SaveToFileStoragePortOut {

  @Value("${app.upload.path}")
  private String localPath;

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.LOCAL;
  }

  @Override
  public void operate(InputStream inputStream, String createSubPath, String createFilename)
      throws IOException {

    Path filePath = Paths.get(localPath, createSubPath);
    Files.createDirectories(filePath);

    Path destinationFile =
        (filePath.resolve(Paths.get(createFilename)).normalize().toAbsolutePath());

    // 상위 디렉토리로 이동하는 경로(Path Traversal) 시도는 차단
    if (!destinationFile.getParent().equals(filePath.toAbsolutePath())) {
      throw new RuntimeException("Cannot store file outside current directory.");
    }

    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    destinationFile.toFile().setWritable(true, false);
    destinationFile.toFile().setReadable(true, false);
  }
}
