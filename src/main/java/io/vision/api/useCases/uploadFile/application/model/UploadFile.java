package io.vision.api.useCases.uploadFile.application.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class UploadFile {
  private String id;
  private FileUploadType type;
  private String path;
  private String name;
  private String originName;
  private Long fileLength;
  private String referURL;
  private String attachmentURL;
}
