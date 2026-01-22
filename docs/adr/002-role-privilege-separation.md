# 2. 역할(Role)과 권한(Privilege)의 분리

* **상태**: 승인됨 (Accepted)
* **날짜**: 2026-01-22
* **작성자**: Vision API Team

## 배경 (Context)

초기 설계에서는 `Role` Enum(`ROLE_USER`, `ROLE_MANAGER`)이 곧 사용자의 권한을 대변했다.
즉, `ROLE_MANAGER`를 가진 사용자는 관리자 기능을 수행할 수 있고, `ROLE_USER`는 사용자 기능을 수행하는 식이었다.

하지만 서비스가 확장됨에 따라 다음과 같은 요구사항이 발생할 수 있음을 인지했다.

1.  **외부 시스템 연동**: 다른 회사나 서비스의 사용자(`ROLE_PARTNER` 등)가 우리 서비스의 일부 기능(예: 사용자 API)만 호출해야 할 수 있다.
2.  **새로운 역할 정의**: `ROLE_SUPER_ADMIN`, `ROLE_OPERATOR` 등 세분화된 역할이 필요할 때, 단순히 Role 이름만으로 접근 제어를 하드코딩하면 유지보수가 어려워진다.

따라서 **"사용자가 누구인가(Role)"**와 **"무엇을 할 수 있는가(Privilege)"**를 분리해야 할 필요성이 제기되었다.

## 결정 (Decision)

우리는 Role-Based Access Control (RBAC)을 보완하여, Role이 구체적인 Privilege들의 집합을 가지는 구조를 채택했다.

### 1. Privilege Enum 도입
실제 실행 가능한 '권한'을 정의하는 `Privilege` Enum을 도입한다.

*   **위치**: `io.vision.api.common.application.enums.Privilege`
*   **정의**:
    ```java
    public enum Privilege {
        ACCESS_USER_API,    // 사용자(Front) API 접근 권한
        ACCESS_MANAGER_API  // 관리자(Manager/Back-office) API 접근 권한
    }
    ```

### 2. Role과 Privilege의 매핑
`Role` Enum이 자신이 포함하는 `Privilege` 목록을 불변 속성으로 가지도록 수정한다.

*   **구조**:
    ```java
    public enum Role {
        ROLE_USER(Set.of(Privilege.ACCESS_USER_API)),
        ROLE_MANAGER(Set.of(Privilege.ACCESS_MANAGER_API)),
        // 추후 확장 예시: 파트너사는 사용자 API 접근 권한만 가짐
        // ROLE_PARTNER(Set.of(Privilege.ACCESS_USER_API))
    }
    ```

## 결과 (Consequences)

### 긍정적 효과
*   **유연성(Flexibility)**: 새로운 역할(Role)이 추가되더라도 코드를(특히 Security 설정이나 API 로직) 수정할 필요 없이, 해당 Role이 가질 `Privilege`만 정의하면 기존 로직이 그대로 동작한다.
*   **재사용성(Reusability)**: `ACCESS_USER_API`와 같은 권한은 여러 Role(`ROLE_USER`, `ROLE_PARTNER` 등)에서 재사용될 수 있다.
*   **명확성(Clarity)**: Role의 정의를 볼 때, 해당 역할이 시스템에서 어떤 권한들을 가지는지 명시적으로 파악할 수 있다.

### 부정적 효과 (또는 고려사항)
*   JWT 토큰 생성 시 Payload가 약간 커질 수 있다. (단, 현재는 Role 이름만 포함하고 검증 시점에 Privilege를 매핑하는 방식을 사용하여 이를 최소화함)
