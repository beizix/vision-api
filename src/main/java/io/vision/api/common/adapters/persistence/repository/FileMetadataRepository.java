package io.vision.api.common.adapters.persistence.repository;

import io.vision.api.common.adapters.persistence.entity.FileMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository
    extends JpaRepository<FileMetadataEntity, String> {}
