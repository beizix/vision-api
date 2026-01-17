package io.api.vision.support;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@Import({DataJpaTestBase.JpaAuditConfig.class})
public abstract class DataJpaTestBase {
  @TestConfiguration
  @EnableJpaAuditing
  static class JpaAuditConfig {}
}
