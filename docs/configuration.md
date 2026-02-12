# Configuration Guide

이 문서는 Vision API 프로젝트의 주요 설정 항목과 환경 구성 방법을 설명합니다.

## 1. 인증 및 JWT 설정 (Security & JWT)

애플리케이션의 보안 인증과 토큰 관리를 위한 설정입니다.

### 1.1 JWT (JSON Web Token)

| 프로퍼티 | 타입 | 기본값 | 설명 |
| :--- | :--- | :--- | :--- |
| `jwt.secret` | String | (필수) | JWT 서명에 사용할 비밀 키 (최소 32자 이상 권장) |
| `jwt.access-token-validity` | Long | 3600000 | Access Token 유효 시간 (밀리초, 기본 1시간) |
| `jwt.refresh-token-validity` | Long | 86400000 | Refresh Token 유효 시간 (밀리초, 기본 24시간) |

---

## 2. 데이터베이스 설정 (Database)

애플리케이션의 데이터 영속성을 위한 데이터베이스 연결 설정입니다.

### 2.1 H2 데이터베이스 (로컬/테스트)

로컬 개발 환경에서는 별도의 설치 없이 H2 인메모리 또는 파일 기반 데이터베이스를 사용합니다.

- **JDBC URL**: `jdbc:h2:file:~/h2/vision/vision;AUTO_SERVER=TRUE`
- **Driver**: `org.h2.Driver`
- **Username**: `sa`
- **Password**: (없음)

---

## 3. 파일 저장소 전략 (File Storage Strategy)

애플리케이션은 파일 저장 시 로컬 서버 저장소 또는 AWS S3 저장소 중 하나 이상을 선택적으로 활성화할 수 있습니다. 각 전략은 `app.storage` 프로퍼티를 통해 제어됩니다.

### 3.1 파일 업로드 임시 경로

저장소 전략(Local/S3)과 관계없이 멀티파트 요청 처리 시 파일을 잠시 보관할 임시 공간이 필요합니다.

- **기본 동작**: 시스템 기본 임시 디렉토리(`java.io.tmpdir`)를 자동으로 사용합니다.
- **경로 변경**: 임시 경로를 변경해야 할 경우, JVM 구동 시 `-Djava.io.tmpdir=/path/to/custom/tmp` 옵션을 추가하십시오. 애플리케이션 설정 클래스에서는 더 이상 임시 경로에 대한 별도의 체크나 로깅을 수행하지 않습니다.

### 3.2 활성화 설정

| 프로퍼티 | 타입 | 기본값 | 설명 |
| :--- | :--- | :--- | :--- |
| `app.storage.local.enabled` | Boolean | `true` | 로컬 서버 저장소 사용 여부 |
| `app.storage.s3.enabled` | Boolean | `false` | AWS S3 저장소 사용 여부 |

> **Tip**: 두 설정을 모두 `true`로 설정하면 로컬 저장소와 S3 저장소를 동시에 사용할 수 있습니다.

### 3.3 로컬 저장소 설정 (Local Storage)

`app.storage.local.enabled`가 `true`인 경우, 다음 설정이 **필수**적으로 요구됩니다.

- **`app.upload.path`**: 파일이 저장될 물리적인 디렉토리 경로입니다.
- **제약 사항**:
    - 해당 값은 반드시 제공되어야 하며, 누락 시 애플리케이션 시작 단계에서 `IllegalStateException`이 발생합니다.
    - 애플리케이션은 실행 시점에 디렉토리를 **직접 생성하지 않습니다.** 인프라 구성 단계에서 해당 경로가 미리 생성되어 있어야 하며, 권한이 부여되어 있어야 합니다. 경로가 존재하지 않을 경우 시작 시 경고 로그가 기록됩니다.

### 3.4 S3 저장소 설정 (AWS S3)

`app.storage.s3.enabled`가 `true`인 경우, AWS S3 연동을 위해 다음 항목들이 `application.yml` 또는 시스템 프로퍼티를 통해 제공되어야 합니다.

#### 필수 설정 항목

| 프로퍼티 | 설명 | 비고 |
| :--- | :--- | :--- |
| `spring.cloud.aws.credentials.access-key` | AWS IAM Access Key | **보안 주의** (아래 참조) |
| `spring.cloud.aws.credentials.secret-key` | AWS IAM Secret Key | **보안 주의** (아래 참조) |
| `spring.cloud.aws.s3.bucket` | S3 버킷 명 | 필수 |
| `spring.cloud.aws.s3.folder` | 버킷 내 기본 저장 폴더 명 | 필수 |
| `spring.cloud.aws.s3.domain` | S3 또는 CloudFront 도메인 주소 | URL 생성 시 사용 |
| `spring.cloud.aws.region.static` | AWS 리전 (예: ap-northeast-2) | 기본값 존재 |

#### ⚠️ 보안 주의사항 (중요)

*   **Access Key 및 Secret Key 보호**: `access-key`와 `secret-key`는 절대 `application.yml`이나 소스 코드에 **직접 문자열로 기술하여 형상 관리(Git)에 포함시키지 마십시오.**
*   **권장 방법**: `bucket`, `folder`, `domain` 등 유출되어도 보안 위협이 낮은 정보는 `application.yml`에 기재할 수 있으나, 자격 증명 정보는 반드시 다음과 같은 방식을 사용해야 합니다.
    *   **환경 변수**: OS 환경 변수를 통한 주입
    *   **시스템 프로퍼티**: 애플리케이션 실행 시 `-D` 옵션으로 전달 (예: `-Dspring.cloud.aws.credentials.access-key=...`)
    *   **Secrets Manager**: AWS Secrets Manager 또는 HashiCorp Vault와 같은 관리 도구 활용

## 4. 로깅 설정 (Logging)

애플리케이션의 로그는 Logback을 통해 관리되며, `src/main/resources/logback-spring.xml`에서 상세 설정을 제어합니다.

### 4.1 로그 출력 및 저장
- **콘솔 출력**: 모든 환경에서 콘솔에 로그가 출력됩니다.
- **파일 저장**: `local` 이외의 프로필(예: `prod`, `dev` 등)이 활성화되었을 때만 파일로 저장됩니다.
- **경로 설정**: `app.logging.path` 프로퍼티를 통해 로그 저장 디렉토리를 지정할 수 있습니다. (기본값: `logs`)

### 4.2 파일 로깅 정책
- **저장 경로**: 애플리케이션 실행 디렉토리 하위의 `./logs/application.log`
- **롤링 정책 (SizeAndTimeBasedRollingPolicy)**:
  - **파일 크기**: 개별 로그 파일이 10MB를 초과하면 새로운 파일로 교체됩니다.
  - **보관 기간**: 최대 30일간 보관합니다.
  - **압축**: 보관된 로그는 `.gz` 형식으로 자동 압축되어 `logs/archived/` 폴더로 이동합니다.
  - **전체 크기 제한**: 모든 로그 파일의 총합이 1GB를 초과하면 오래된 파일부터 삭제됩니다.

### 4.3 프로필별 권장 설정
- **Local**: `io.vision.api` 패키지의 로그 레벨이 `DEBUG`로 설정되어 상세한 개발 정보를 볼 수 있습니다.
- **운영 (Non-local)**: 로그 레벨은 `INFO`가 기본이며, 콘솔과 파일에 동시에 기록됩니다.

---

## 5. 타임존 설정 (Timezone)

애플리케이션 내의 시간 처리를 위한 전역 타임존 설정입니다.

| 프로퍼티 | 타입 | 기본값 | 설명 |
| :--- | :--- | :--- | :--- |
| `app.timezone` | String | `Asia/Seoul` | 애플리케이션의 기본 타임존 (예: UTC, Asia/Seoul) |

- **기본 동작**: 별도의 설정이 없으면 `Asia/Seoul` 시간을 기본으로 사용합니다.
- **설정 변경**: 서버 실행 시 `-Dapp.timezone=UTC`와 같이 시스템 프로퍼티로 전달하거나 `application.yml`을 통해 변경 가능합니다.
