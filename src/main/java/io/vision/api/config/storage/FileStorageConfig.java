package io.vision.api.config.storage;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "app.storage.local.enabled", havingValue = "true", matchIfMissing = true)
public class FileStorageConfig implements WebMvcConfigurer {

  @Value("${app.upload.path:#{null}}")
  private String publicPath;

  @Value("${app.upload.tmpDir:#{null}}")
  private String tmpPath;

  @PostConstruct
  public void initialize() throws IOException {
    log.info("FileStorageConfig - initialize : app.upload.path is {}", publicPath);
    log.info("FileStorageConfig - initialize : app.upload.tmpDir is {}", tmpPath);

    if (publicPath != null && !publicPath.isBlank()) {
      createDirectory(publicPath);
    }

    if (tmpPath != null && !tmpPath.isBlank()) {
      createDirectory(tmpPath);
    }
  }

  private void createDirectory(String path) throws IOException {
    try {
      Files.createDirectories(Paths.get(path));
    } catch (IOException e) {
      throw new IOException("Could not initialize storage location: " + path, e);
    }
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (publicPath != null && !publicPath.isBlank()) {
      String resourcePath = "file:" + Paths.get(publicPath).toAbsolutePath().toString() + "/";
      registry.addResourceHandler("/uploads/**")
          .addResourceLocations(resourcePath);
      
      log.info("Mapped '/uploads/**' to local resource: {}", resourcePath);
    }
  }
}
