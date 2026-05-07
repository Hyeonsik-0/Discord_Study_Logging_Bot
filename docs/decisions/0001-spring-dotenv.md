# 0001. Discord 토큰을 .env 파일로 관리

- 상태: Accepted
- 일자: 2026-05-07

## 맥락 (Context)

기존에는 Windows + IntelliJ 환경에서 Discord Bot Token 을 IntelliJ Run Configuration 의 환경변수로 등록했고, 그 값이 `.idea/workspace.xml` 에 저장되어 있었다.

이제 다음 요구사항이 생겼다.

1. Mac / Windows 양쪽에서 작업할 수 있어야 함
2. 프로젝트를 GitHub public 저장소에 push 해야 함
3. 토큰은 절대 git 저장소에 들어가서는 안 됨

`.idea/` 는 이미 `.gitignore` 에 포함되어 있어 토큰이 git history 에 leak 된 적은 없으나, 머신 간 이동성과 일관성이 부족했다.

## 결정 (Decision)

`.env` 파일에 토큰을 보관하고, [`me.paulschwarz:spring-dotenv`](https://github.com/paulschwarz/spring-dotenv) 라이브러리를 통해 Spring Boot 가 자동으로 로드하도록 한다.

- `.env` 는 `.gitignore` 로 추적 차단
- `.env.example` 은 commit 하여 새 환경 셋업 시 템플릿으로 사용
- `application.properties` 는 `${DISCORD_TOKEN}` 으로 환경변수를 참조

## 대안 (Alternatives Considered)

### A. IntelliJ EnvFile 플러그인
- 장점: 의존성 0
- 단점: 머신마다 IntelliJ 플러그인 설치/설정 필요. CLI/Docker 실행 경로에서는 사용 불가

### B. Docker Compose `env_file` 만 사용
- 장점: 별도 라이브러리 불필요
- 단점: 앱이 Docker 안에서만 동작. IDE 직접 실행 시 별도 설정 필요

### C. 시스템 환경변수 직접 export
- 장점: 의존성 0
- 단점: Windows / Mac 설정 방식 다름. 셸 재시작 시 휘발

### D. spring-dotenv ★ 채택
- 장점: IDE에서 Run 한 번으로 동작 (Win/Mac 동일). `.env` 한 파일로 모든 실행 경로(IDE, gradle, docker) 가 같은 값 참조 가능
- 단점: 의존성 1개 추가

## 결과 (Consequences)

### 긍정
- 머신 간 이동 시 `.env` 만 새로 작성하면 됨
- 새 개발자 온보딩 절차가 `.env.example` → `.env` 복사로 단순화
- IDE / gradle / Docker 가 동일한 환경 변수 소스 사용

### 부정
- 의존성 1개 추가
- 신규 변수 추가 시 `.env.example` 동기화 필요 (수동)

## 참고
- [spring-dotenv GitHub](https://github.com/paulschwarz/spring-dotenv)
- [The Twelve-Factor App: Config](https://12factor.net/config)
