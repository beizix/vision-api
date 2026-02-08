package io.vision.api.useCases.storage.getFileResource.application;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileResourceService implements GetFileResourceUseCase {
  @Override
  public String operate(UUID fileUuid) {
    return "";
  }
}
