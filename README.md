# 🪪 playground-auth

> [!Note]
> **회원 서비스와 인증 게이트웨이·인가 라이브러리 구축**

`Gateway` · `Spring Security` · `JWT` · `OAuth2` · `Github Packages`

## 🛠️ 기술 스택

| 분류        | 사용                                      |
|-------------|-------------------------------------------|
| Language    | Java 25                                   |
| Framework   | Spring Boot 4, Spring Cloud Gateway       |
| Build       | Gradle 9 · Kotlin DSL                     |
| Security    | Spring Security, JWT, OAuth2 Client       |
| Persistence | MySQL 9, Spring Data JPA                  |
| Cache       | Redis 7, Spring Data Redis                |
| Infra       | Gradle 9, Docker Compose, Github Packages |

## 🏗️ 프로젝트 구조 및 설명

```
playground-auth/
├─ auth-gateway/                # 인증 담당
├─ security-core/               # 인가 담당
├─ user-service/                # 회원 서비스
├─ docker-compose.yml
└─ .env
```

* 인증·인가
  - 인증 게이트웨이: access token 유효성 검증
  - 인가 라이브러리: 유저 상태 및 권한 검증
  - 인증 게이트웨이에서 서비스로 유저 정보를 넘길 때, 2가지 방법으로 전달 가능 (AUTH_MODE)
    1. JWT 토큰을 파싱해, 커스텀 헤더로 정보를 주입해서 전달 (x-header)
    2. JWT 토큰을 그대로 전달하고, 서비스에서 재파싱 후 정보 추출 (jwt-token)
* 회원 서비스: 일반 or 소셜 회원가입, 로그인, 로그아웃
  - 2종의 소셜 로그인 지원 (Google, Naver)
  - 소셜 회원가입 후, 추가 정보 입력해야 서비스 사용 가능
* [기술적 의사결정 과정](https://hyuns2.notion.site/playground-auth-3a32ac90a22f8024ac57efbf52239ac5)

## 🚀 실행 방법

### ▶️ Run

```bash
docker compose up -d
```

### ⏹️  Stop

```bash
docker compose down
```
