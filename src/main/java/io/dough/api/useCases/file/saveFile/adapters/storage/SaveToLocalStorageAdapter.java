package io.dough.api.useCases.file.saveFile.adapters.storage;

import io.dough.api.common.application.utils.MessageUtils;
import io.dough.api.useCases.file.saveFile.application.SaveToFileStorage;
import io.dough.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
    name = "app.storage.local.enabled",
    havingValue = "true",
    matchIfMissing = true)
public class SaveToLocalStorageAdapter implements SaveToFileStorage {
  private final MessageUtils messageUtils;

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
      throw new IllegalArgumentException(
          messageUtils.getMessage("exception.file.path_traversal", new Object[] {createFilename}));
    }

    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    destinationFile.toFile().setWritable(true, false);
    destinationFile.toFile().setReadable(true, false);
  }
}
