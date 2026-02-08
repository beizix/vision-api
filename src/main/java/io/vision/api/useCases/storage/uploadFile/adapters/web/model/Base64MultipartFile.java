package io.vision.api.useCases.storage.uploadFile.adapters.web.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class Base64MultipartFile implements MultipartFile {

  private final byte[] content;
  private final String filename;
  private final String contentType;

  public Base64MultipartFile(String base64String) {
    // Expected format: data:image/png;base64,iVBORw0KGgo...
    String[] parts = base64String.split(",");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid Base64 string format");
    }

    String contentTypePart = parts[0];
    String base64Data = parts[1];

    this.contentType = contentTypePart.split(":")[1].split(";")[0];
    this.content = Base64.getDecoder().decode(base64Data);

    String extension = this.contentType.split("/")[1];
    this.filename = UUID.randomUUID().toString() + "." + extension;
  }

  @Override
  public String getName() {
    return "file";
  }

  @Override
  public String getOriginalFilename() {
    return this.filename;
  }

  @Override
  public String getContentType() {
    return this.contentType;
  }

  @Override
  public boolean isEmpty() {
    return content == null || content.length == 0;
  }

  @Override
  public long getSize() {
    return content.length;
  }

  @Override
  public byte[] getBytes() throws IOException {
    return content;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(content);
  }

  @Override
  public void transferTo(File dest) throws IOException, IllegalStateException {
    Files.write(dest.toPath(), content);
  }

  @Override
  public void transferTo(Path path) throws IOException, IllegalStateException {
    Files.write(path, content);
  }
}
