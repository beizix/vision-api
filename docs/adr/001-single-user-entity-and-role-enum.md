# 1. 단일 UserEntity 전략 및 Role Enum 도입

* **상태**: 승인됨 (Accepted)
* **날짜**: 2026-01-17
* **작성자**: Dough API Team

## 배경 (Context)

프로젝트 초기 단계에서 인증(Authentication) 및 인가(Authorization) 구조를 설계하던 중, 다음과 같은 고민이 발생했다.

1.  **관리자(Manager)와 일반 사용자(User)의 구분**: 별도의 `ManagerEntity`와 `UserEntity`로 분리해야 하는가, 아니면 하나의 `UserEntity`로 통합해야 하는가?
2.  **권한(Role) 관리 방식**: 사용자 권한을 `String`으로 관리할 것인가, `Enum`으로 관리할 것인가?
3.  **확장성 고려**: 향후 소셜 로그인(OAuth2) 도입 시 현재 구조가 유효한가?

## 결정 (Decision)

우리는 다음과 같은 아키텍처를 채택하기로 결정했다.

### 1. 단일 UserEntity 전략 (Single Entity Strategy)
관리자와 일반 사용자를 별도의 엔티티로 분리하지 않고, **하나의 `UserEntity`로 관리**한다.

*   **이유**:
    *   **인증의 통일성**: 관리자든 사용자든 "ID/PW로 식별되는 주체"라는 본질은 같다. 엔티티를 분리하면 로그인, JWT 발급, 보안 설정 로직이 불필요하게 중복된다.
    *   **인가의 유연성**: 접근 제어는 엔티티 분리가 아닌, **권한(Role)**을 통해 제어하는 것이 Spring Security의 표준 패턴에 부합한다.
    *   **데이터 모델링**: 현재 단계에서 관리자와 사용자의 데이터 속성 차이가 크지 않다.

### 2. Role Enum 도입
권한을 단순 문자열(`String`)이 아닌 **`Enum`**으로 정의하여 관리한다.

*   **위치**: `io.dough.api.common.application.enums.Role` (Hexagonal Architecture의 Application Layer)
*   **정의**:
    ```java
    public enum Role {
        USER,
        MANAGER
    }
    ```
*   **이유**:
    *   **타입 안전성(Type Safety)**: 오타 방지 및 컴파일 시점 검증 가능.
    *   **유지보수**: 권한 변경 시 Enum 정의만 수정하면 됨.
    *   **JPA 매핑**: `@Enumerated(EnumType.STRING)`을 사용하여 DB에는 직관적인 문자열로 저장하되, 코드에서는 Enum으로 안전하게 사용.

### 3. 소셜 로그인 확장 전략 (Future Proof)
향후 소셜 로그인 도입 시에도 `UserEntity`를 유지하되, **1:1 관계의 별도 테이블(예: `SocialAccount`)로 소셜 정보를 분리**하는 방식을 채택한다. 이를 통해 관리자 계정 등 소셜 정보가 불필요한 레코드의 데이터 희소성(Sparsity) 문제를 방지한다.

## 결과 (Consequences)

### 긍정적 효과
*   인증 로직(JWT, LoginService)을 단일화하여 유지보수성이 크게 향상되었다.
*   `Role` Enum 사용으로 코드의 안정성과 가독성이 높아졌다.
*   초기 개발 속도를 저해하지 않으면서도, 미래의 확장성(소셜 로그인 등)을 고려한 유연한 구조를 확보했다.

### 부정적 효과 (또는 고려사항)
*   향후 관리자 전용 필드가 급격히 늘어날 경우 `UserEntity`가 비대해질 수 있다. 이 경우 1:1 관계의 `ManagerProfile` 테이블 분리를 고려해야 한다.
