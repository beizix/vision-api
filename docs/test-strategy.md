# Test Strategy Guide

이 문서는 Dough API 프로젝트의 테스트 전략과 계층별 테스트 작성 방법을 설명합니다.

## 1. 개요 (Overview)

우리 프로젝트는 **Outside-In TDD (London School)** 방식을 지향하며, 헥사고날 아키텍처의 각 계층을 독립적으로 검증하는 것을 원칙으로 합니다.

- **목표**: 비즈니스 로직의 정확성 보장, 아키텍처 규칙 준수, 안정적인 리팩토링 환경 제공
- **핵심 도구**: JUnit 5, AssertJ, Mockito, ArchUnit

---

## 2. 테스트 계층 및 유형 (Testing Layers)

### 2.1 Web Adapter Test (Inbound)
HTTP 요청 매핑, 파라미터 검증, 응답 상태 코드, 그리고 UseCase 호출 여부를 검증합니다.

- **특징**: `@WebMvcTest`를 사용하며 보안 필터가 포함된 슬라이스 테스트를 수행합니다.
- **Base Class**: `io.dough.api.support.WebMvcTestBase`
- **검증 대상**:
  - URL 및 HTTP Method 매핑
  - RequestBody(DTO) 역직렬화 및 유효성 검사
  - 비즈니스 로직(UseCase) 호출 여부 및 파라미터 전달
  - 성공/실패 시의 HTTP 상태 코드 및 응답 본문

### 2.2 Application Layer Test (Core)
순수 비즈니스 로직과 도메인 규칙을 검증합니다.

- **특징**: Spring Context 없이 `MockitoExtension`만을 사용하는 순수 단위 테스트입니다. 가장 실행 속도가 빠릅니다.
- **검증 대상**:
  - 비즈니스 유스케이스 실행 결과
  - 예외 처리 및 에러 메시지
  - 출력 포트(PortOut)와의 상호작용 (호출 횟수, 파라미터 등)

### 2.3 Persistence Adapter Test (Outbound)
실제 데이터베이스와의 연동 및 쿼리 동작을 검증합니다.

- **특징**: `@DataJpaTest`를 사용하여 H2 인메모리 DB에서 테스트를 수행합니다.
- **Base Class**: `io.dough.api.support.DataJpaTestBase`
- **검증 대상**:
  - Entity 매핑 및 제약 조건 (Unique, Not Null 등)
  - Custom Repository 쿼리 동작
  - 감사(Auditing) 필드(`createdAt`, `updatedAt`) 자동 업데이트 여부

### 2.4 Architecture Test (ArchUnit)
패키지 간의 의존성 규칙이 헥사고날 아키텍처 원칙을 준수하는지 자동으로 검증합니다.

- **핵심 클래스**: `io.dough.api.ApiArchTest`
- **검증 규칙**:
  - `application`(Core) 계층은 어떠한 외부 계층도 참조해서는 안 됨.
  - `web`과 `persistence` 어댑터는 서로를 직접 참조할 수 없음.

---

## 3. 테스트 작성 원칙 (Principles)

### 3.1 명명 규칙 (Naming)
- `@DisplayName`을 사용하여 테스트의 의도를 문장으로 표현합니다.
- **형식**: `@DisplayName("Scenario: <성공/실패> - <설명>")`

### 3.2 구조 (Structure)
`Given - When - Then` 주석을 사용하여 테스트 단계를 명확히 구분합니다.

```java
@Test
@DisplayName("Scenario: 성공 - 유효한 요청 시 사용자를 저장한다")
void success_test() {
    // Given (준비: Mock 설정, 데이터 준비)

    // When (실행: 테스트 대상 메서드 호출)

    // Then (검증: 결과값 확인, Mock 호출 확인)
}
```

### 3.3 검증 (Assertion)
- **AssertJ**를 사용하여 읽기 쉬운 단언문을 작성합니다. (`assertThat(...)`)
- 상태 검증(State Verification)뿐만 아니라 행위 검증(Behavior Verification, `verify(...)`)도 적절히 혼합하여 사용합니다.

---

## 4. 테스트 인프라 (Infrastructure)

### 4.1 공통 설정
- `src/test/resources/application.yml`에 테스트 전용 설정을 관리합니다.
- 로컬 파일 시스템 대신 `${java.io.tmpdir}`를 활용하여 테스트 간의 독립적인 저장 경로를 확보합니다.

### 4.2 Mocking 가이드
- 하위 계층은 적극적으로 Mocking하여 테스트 대상의 로직에만 집중합니다.
- Spring Context가 필요한 테스트에서는 `@MockitoBean`을, 순수 단위 테스트에서는 `@Mock`을 사용합니다.

---

## 5. 실행 방법 (Execution)

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스만 실행
./gradlew test --tests io.dough.api.useCases.auth.signup..*
```
