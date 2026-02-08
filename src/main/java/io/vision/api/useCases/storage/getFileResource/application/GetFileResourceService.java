package io.vision.api.useCases.storage.getFileResource.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileResourceService implements GetFileResourceUseCase {
  @Override
  public String operate(String fileUuid) {
    return "";
  }
}
