package io.api.vision.common.adapters.persistence.component;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 생성 및 수정 기록을 위한 공용 엔티티 속성. Admin 앱의 경우, LoginUserAuditAware.java (AuditorAware 구현체) 가 반환한 현재 작성자
 * 정보를 담는다.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public class AuditEntity {
  @CreatedDate
  @Column(updatable = false, comment = "생성일", precision = 6)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(updatable = false, comment = "생성자 아이디")
  private String createdBy;

  @LastModifiedDate
  @Column(comment = "수정일", precision = 6)
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(comment = "수정자 아이디")
  private String updatedBy;

  @Column(name = "deleted_at", comment = "삭제 일시", precision = 6)
  private LocalDateTime deletedAt;

  @Column(name = "deleted_by", comment = "삭제자 아이디")
  protected String deletedBy;

  @Column(name = "deleted", comment = "삭제 여부")
  private boolean deleted = false;
}
