package io.dough.api.useCases.user.saveMyProfileImg.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.saveMyProfileImg.application.UpdateUserProfileImg;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateUserProfileImgPersistAdapter implements UpdateUserProfileImg {

  private final UserRepository userRepository;
  private final FileMetadataRepository fileMetadataRepository;

  @Override
  @Transactional
  public void operate(UUID userId, UUID fileId) {
    UserEntity user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    FileMetadataEntity fileMetadata =
        fileMetadataRepository
            .findById(fileId)
            .orElseThrow(() -> new IllegalArgumentException("File metadata not found"));

    user.setProfileImage(fileMetadata);
  }
}
