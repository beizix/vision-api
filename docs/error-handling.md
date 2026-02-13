# Error Handling Guide

이 문서는 Dough API 프로젝트의 공통 예외 처리 방식과 응답 스펙을 설명합니다.

## 1. 개요 (Overview)

우리 시스템은 사용자에게 일관된 에러 응답을 제공하기 위해 **중앙 집중식 예외 처리** 방식을 채택하고 있습니다. Spring의 `@RestControllerAdvice`를 활용하여 모든 API 계층에서 발생하는 예외를 한곳에서 관리합니다.

- **핵심 클래스**: `GlobalWebExceptionHandler.java`
- **응답 모델**: `ErrorResponse.java` (Java Record 타입)

---

## 2. 응답 스펙 (Response Specification)

모든 에러 응답은 다음과 같은 공통 구조를 가집니다.

| 필드명 | 타입 | 설명 | 예시 |
| :--- | :--- | :--- | :--- |
| `timestamp` | String | 에러가 발생한 시각 (ISO 8601) | `"2026-02-12T09:00:00.123456"` |
| `status` | int | HTTP 상태 코드 | `400`, `401`, `500` |
| `error` | String | HTTP 상태 이름 (Reason Phrase) | `"Bad Request"`, `"Internal Server Error"` |
| `message` | String | 구체적인 에러 메시지 (사용자 알림용) | `"파일 확장자가 존재하지 않습니다."` |
| `path` | String | 에러가 발생한 요청 API 경로 | `"/v1/api/files/upload"` |

### JSON 예시
```json
{
  "timestamp": "2026-02-12T09:01:45.123",
  "status": 400,
  "error": "Bad Request",
  "message": "'test.exe' - 허용되지 않는 파일 확장자입니다.",
  "path": "/v1/api/files/upload"
}
```

---

## 3. 예외별 처리 전략 (Exception Strategies)

### 3.1 400 Bad Request (잘못된 요청)
클라이언트의 입력값이 비즈니스 규칙에 어긋나거나 필수 조건이 누락된 경우에 해당합니다.

- **처리 예외**:
  - `IllegalArgumentException`: 비즈니스 로직 검증 실패 시 사용 (가장 권장됨)
  - `NoSuchElementException`: 요청한 자원을 찾을 수 없거나 조회가 실패했을 때 사용

### 3.2 500 Internal Server Error (서버 오류)
시스템 내부 로직 오류, DB 연결 실패, 입출력 장애 등 예측하지 못한 예외가 발생한 경우입니다.

- **처리 예외**: `Exception` (최상위 예외)
- **특이 사항**: 서버 로그에는 전체 스택 트레이스를 기록하지만, 보안을 위해 클라이언트에게는 필요한 정보만 필터링하여 전달합니다.

---

## 4. 개발 가이드라인 (Developer Guidelines)

1.  **구체적인 예외 사용**: 단순히 `RuntimeException`을 던지기보다, 의미가 명확한 `IllegalArgumentException`이나 커스텀 예외를 사용하십시오.
2.  **다국어 메시지 사용**: `message` 필드는 클라이언트에서 사용자에게 직접 노출될 수 있습니다. 소스코드에 한국어/영어를 직접 하드코딩하지 말고, `MessageUtils`를 사용하여 다국어 처리를 적용하십시오. (상세 내용은 [i18n-messaging.md](./i18n-messaging.md) 참고)
3.  **Fail-Fast**: 검증 로직은 유스케이스나 서비스의 상단에 배치하여, 잘못된 데이터가 시스템 깊숙이 들어오기 전에 즉시 예외를 발생시키십시오.
4.  **로깅**: `GlobalWebExceptionHandler`에서 예외 발생 사실을 에러 레벨로 로깅하므로, 서비스 레이어에서 단순히 예외를 다시 던지는 용도로만 로그를 남길 필요는 없습니다.
