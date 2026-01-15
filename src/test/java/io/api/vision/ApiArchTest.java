package io.api.vision;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiArchTest {

  final JavaClasses classes =
      new ClassFileImporter()
          .withImportOption(new ImportOption.DoNotIncludeTests())
          .importPackages("io.api.vision..");

  final Architectures.LayeredArchitecture layeredArchitecture =
      layeredArchitecture()
          .consideringOnlyDependenciesInAnyPackage("io.api.vision..")
          // `(외부) 인바운드 어뎁터` 계층 정의
          .layer("inboundAdapters")
          .definedBy("..adapters.web..")
          // `(내부) 코어` 계층 정의
          .layer("core")
          .definedBy("..application..")
          // `(외부) 아웃바운드 어뎁터` 계층 정의
          .layer("outboundAdapters")
          .definedBy("..adapters.persistence..", "..adapters.ai..")
          // `(설정) 구성 요소` 계층 정의
          .layer("config")
          .definedBy("..config..");

  @DisplayName("`(외부) 인바운드 어뎁터` 계층은 오직 `(내부) 코어` 계층 만 참조할 수 있다.")
  @Test
  void webLayerMayOnlyAccessToApplication() {
    layeredArchitecture.whereLayer("inboundAdapters").mayOnlyAccessLayers("core").check(classes);
  }

  @DisplayName("`(외부) 인바운드 어뎁터` 계층은 오직 `(설정) 구성 요소` 계층 만 접근을 허용 한다.")
  @Test
  void webLayerMayOnlyBeAccessedByLayers() {
    layeredArchitecture
        .whereLayer("inboundAdapters")
        .mayOnlyBeAccessedByLayers("config")
        .check(classes);
  }

  @DisplayName("`(외부) 아웃바운드 어덥터` 계층은 오직 `(내부) 코어` 계층 만 참조할 수 있다.")
  @Test
  void persistenceLayerMayOnlyAccessToApplication() {
    layeredArchitecture.whereLayer("outboundAdapters").mayOnlyAccessLayers("core").check(classes);
  }

  @DisplayName("`(외부) 아웃바운드 어덥터` 계층은 오직 `(내부) 코어` 과 `(설정) 구성 요소` 계층 만 접근을 허용 한다.")
  @Test
  void mayOnlyBeAccessedByLayers() {
    layeredArchitecture
        .whereLayer("outboundAdapters")
        .mayOnlyBeAccessedByLayers("core", "config")
        .check(classes);
  }

  @DisplayName("`(내부) 코어` 계층은 어느 계층도 참조 하지 않는다(중요).")
  @Test
  void applicationMayNotAccessAnyLayer() {
    layeredArchitecture.whereLayer("core").mayNotAccessAnyLayer().check(classes);
  }

  @DisplayName("`(내부) 코어` 계층은 모든 계층의 접근을 허용 한다.")
  @Test
  void applicationLayerMayOnlyBeAccessedByLayers() {
    layeredArchitecture
        .whereLayer("core")
        .mayOnlyBeAccessedByLayers("inboundAdapters", "outboundAdapters", "config")
        .check(classes);
  }

  @DisplayName("`(설정) 구성 요소` 계층은 모든 계층을 참조 한다.")
  @Test
  void configMayOnlyAccessLayers() {
    layeredArchitecture
        .whereLayer("config")
        .mayOnlyAccessLayers("inboundAdapters", "core", "outboundAdapters")
        .check(classes);
  }

  @DisplayName("`(설정) 구성 요소` 계층은 모든 계층의 접근을 허용하지 않는다.")
  @Test
  void configMayNotAccessAnyLayer() {
    layeredArchitecture.whereLayer("config").mayNotBeAccessedByAnyLayer().check(classes);
  }
}
