package io.vision.api.useCases.uploadFile.adapters.storage;

import io.vision.api.useCases.uploadFile.application.SaveToFileStoragePortOut;
import io.vision.api.useCases.uploadFile.application.model.FileStorageType;
import jakarta.annotation.PostConstruct;
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
  private String publicPath;

  @Value("${app.upload.tmpDir}")
  private String tmpPath;

  @PostConstruct
  public void initialize() throws Exception {
    log.info("SaveToFileStorageAdapter - initialize : app.upload.path is {}", publicPath);
    log.info("SaveToFileStorageAdapter - initialize : app.upload.tmpDir is {}", tmpPath);

    try {
      Files.createDirectories(Paths.get(publicPath));
      Files.createDirectories(Paths.get(tmpPath));
    } catch (IOException e) {
      throw new IOException("Could not initialize storage", e);
    }
  }

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.LOCAL;
  }

  @Override
  public void operate(InputStream inputStream, String createSubPath, String createFilename)
      throws IOException {

    Path filePath = Paths.get(publicPath, createSubPath);
    Files.createDirectories(filePath);

    Path destinationFile =
        (filePath.resolve(Paths.get(createFilename)).normalize().toAbsolutePath());

    if (!destinationFile.getParent().equals(filePath.toAbsolutePath())) {
      // This is a security check
      throw new RuntimeException("Cannot store file outside current directory.");
    }

    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    destinationFile.toFile().setWritable(true, false);
    destinationFile.toFile().setReadable(true, false);
  }
}
