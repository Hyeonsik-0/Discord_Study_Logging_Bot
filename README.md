# Discord Study Bot

Spring Boot + JDA 기반 스터디 시간 기록 봇

## Goal
- Discord 음성 채널 기반 학습 시간 자동 기록
- Spring 백엔드 구조 학습
- TDD 적용

## Tech Stack
- Java 17 / Spring Boot / Gradle
- JDA (Java Discord API)
- MySQL + Spring Data JPA
- Docker / Docker Compose

자세한 내용은 [docs/tech-stack.md](docs/tech-stack.md) 참고.

## Setup

### Prerequisites
- Java 17+
- Docker / Docker Compose
- Discord Bot Token ([Discord Developer Portal](https://discord.com/developers/applications) 에서 발급)

### Steps
1. 저장소 클론
   ```bash
   git clone https://github.com/Hyeonsik-0/Discord_Study_Logging_Bot.git
   cd Discord_Study_Logging_Bot
   ```

2. `.env` 파일 생성 및 토큰 설정
   ```bash
   cp .env.example .env
   # .env 열어서 DISCORD_TOKEN 채우기
   ```

3. MySQL 컨테이너 실행
   ```bash
   docker compose up -d
   ```

4. 봇 실행
   ```bash
   ./gradlew bootRun
   ```
   또는 IntelliJ에서 `StudybotApplication` Run.

## Features (MVP)

본 프로젝트의 초기 MVP는 사용자의 Discord 음성 채널 입장/퇴장 이벤트를 기반으로 학습 시간을 자동 기록하고, Slash Command를 통해 개인 학습 시간 및 서버 내 학습 순위를 조회하는 기능을 제공한다.

### MVP 기능 목록

| 구분 | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| 이벤트 처리 | 음성 채널 입장 감지 | 사용자의 학습 세션 시작 | 필수 |
| 이벤트 처리 | 음성 채널 퇴장 감지 | 학습 세션 종료 및 시간 계산 | 필수 |
| Slash Command | /today | 오늘 학습 시간 조회 | 필수 |
| Slash Command | /week | 이번 주 학습 시간 조회 | 필수 |
| Slash Command | /ranking | 이번 주 서버 내 학습 순위 조회 | 필수 |
| Slash Command | /status | 현재 학습 중 여부 조회 | 권장 |
| Slash Command | /help | 명령어 목록 조회 | 권장 |
| Slash Command | /ping | 봇 연결 상태 확인 | 개발용 |

### 기능 상세

#### 음성 채널 학습 시간 기록

사용자가 음성 채널에 입장하면 학습 세션을 시작하고, 퇴장하면 세션을 종료한다.  
세션 종료 시 시작 시간과 종료 시간의 차이를 계산하여 학습 시간으로 저장한다.

#### 학습 시간 조회

사용자는 Slash Command를 통해 본인의 학습 시간을 조회할 수 있다.  
초기 버전에서는 일간 및 주간 단위 조회를 제공하며, 추후 월간/연간 조회로 확장 가능하도록 설계한다.

#### 학습 순위 조회

서버 내 사용자들의 주간 누적 학습 시간을 기준으로 순위를 제공한다.  
초기에는 주간 랭킹만 제공하고, 추후 일간/월간/연간 랭킹으로 확장할 수 있다.

## Documentation

- [tech-stack.md](docs/tech-stack.md) — 기술 스택과 선택 이유
- [architecture.md](docs/architecture.md) — 시스템 아키텍처 및 계층 구조
- [agent-usage.md](docs/agent-usage.md) — AI Agent 활용 전략
- [decisions/](docs/decisions/) — Architecture Decision Records
- [devlog/](docs/devlog/) — 개발 일지
