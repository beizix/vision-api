# Dough API

<p align="center">
  <img src="./docs/img/dough.png" width="400" alt="Dough API Logo">
</p>

## Dough API: The Starter Kit for Scalable Backend

**Dough API**는 빵의 토대가 되는 반죽처럼, 비즈니스 로직에만 집중할 수 있도록 돕는 **Backend Boilerplate**입니다.

단순히 코드를 모아놓은 것을 넘어, **헥사고날 아키텍처**를 기반으로 설계되어 인프라 변화에 유연하며, **London School TDD** 방식을 엄격히 준수하여 '실패 없는 배포'를 지향합니다.

- **Ready-to-Use:** 파일 관리 및 보안 인증 시스템 즉시 가동 가능
- **High Maintainability:** 도메인 중심 설계로 비즈니스 요구사항 변경에 최적화
- **Trustworthy:** 촘촘한 테스트 코드로 검증된 안정성


## 🌟 주요 기능

-   **RESTful API**: 표준화된 API 인터페이스 제공
-   **사용자 인증 및 권한 관리**: Spring Security와 JWT를 이용한 보안 강화
-   **파일 스토리지 관리**: 로컬 파일 시스템 및 AWS S3 멀티 스토리지 지원
-   **강력한 유효성 검사**: Apache Tika를 활용한 파일 MIME-Type 검증

## 🛠 기술 스택

-   **Framework**: Spring Boot 4.0.1
-   **Language**: Java 21
-   **Architecture**: Hexagonal Architecture
-   **Database**: H2 (Default), JPA/Hibernate를 지원하는 모든 RDBMS와 연동
-   **Storage**: Local File System, AWS S3 (Object Storage)
-   **Testing**: JUnit 5, Mockito, AssertJ (TDD)
-   **Build Tool**: Gradle

---

## 🚀 개발 및 실행 가이드

### 1. 애플리케이션 실행
- **IDE 구동**: `io.dough.api.DoughApplication` 클래스의 `main` 메서드를 실행합니다.

### 2. H2 데이터베이스 접속
-   **JDBC URL**: `jdbc:h2:file:~/h2/dough/dough-api;AUTO_SERVER=TRUE`
-   **User**: `sa` / **Password**: (없음)

## 📂 프로젝트 구조 및 문서 가이드

### 1. 주요 가이드 문서
애플리케이션 구동 및 개발 전 아래 문서들을 반드시 확인해 주세요.
-   **[필수 설정 가이드](./docs/configuration.md)**: 구동을 위해 반드시 필요한 `jwt.secret` 등 환경 변수 및 프로퍼티 설정 방법 안내
-   **[도메인 용어 사전](./docs/domain-glossary.md)**: 프로젝트에서 사용하는 핵심 비즈니스 용어 정의
-   **[에러 핸들링 가이드](./docs/error-handling.md)**: 공통 에러 응답 규격 및 예외 처리 전략
-   **[테스트 전략](./docs/test-strategy.md)**: Outside-In TDD 접근 방식 및 계층별 테스트 원칙

### 2. 프로젝트 구조 (Hexagonal Architecture)
```
io.dough.api/
|-- common        # 공통 도메인, 엔티티, 리포지토리
`-- useCases      # 유스케이스 기반 도메인 로직
    |-- auth      # 인증 (Login, Signup, JWT)
    `-- file      # 파일 처리 (Save, GetURL)
```

상세한 아키텍처 결정 사항(ADR) 등은 [docs](./docs/) 디렉토리에서 확인할 수 있습니다.

### 3. API 문서 (Swagger)
애플리케이션 구동 후 아래 주소를 통해 대화형 API 문서를 확인할 수 있습니다.
-   **Swagger UI**: /swagger-ui/index.html

## 테스트 실행

#### 전체 테스트 실행
```bash
./gradlew test
```

#### 특정 테스트 케이스 실행
```bash
./gradlew test --tests io.dough.api.config.security.SecurityConfigTest
```

##  코드 스타일 관리 (Spotless)
이 프로젝트는 Google Java Format을 준수하며 Spotless를 통해 코드 스타일을 강제합니다.
```bash
./gradlew spotlessApply
```
