# 기술 스택

본 프로젝트의 기술 선택과 그 이유를 정리한다.

## 1. Backend (Core Application)
- **Language**: Java 17+
- **Framework**: Spring Boot
- **Build Tool**: Gradle

### 선택 이유
- 계층형 아키텍처 (Controller / Service / Repository) 설계 학습
- REST API 및 이벤트 기반 처리 구조 구현 가능
- 실무에서 널리 사용되는 백엔드 프레임워크

---

## 2. Discord Bot 연동
- **Library**: JDA (Java Discord API)

### 선택 이유
- Java 기반으로 Spring과 통합 용이
- Discord Gateway 기반 이벤트 처리 가능
- 실시간 이벤트 기반 시스템 구조 학습

---

## 3. Database
- **DBMS**: MySQL
- **ORM**: Spring Data JPA (Hibernate)

### 선택 이유
- 범용성이 높은 관계형 데이터베이스
- JPA를 통한 객체-관계 매핑 학습
- 트랜잭션 및 데이터 무결성 관리 경험

---

## 4. Infrastructure / Deployment
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **Environment Management**: `.env` (via [spring-dotenv](https://github.com/paulschwarz/spring-dotenv))

### 선택 이유
- 개발/운영 환경 일관성 확보
- DB + Application 통합 실행 환경 구성
- 실제 서비스 배포와 유사한 구조 경험

자세한 결정 배경은 [decisions/0001-spring-dotenv.md](decisions/0001-spring-dotenv.md) 참고.

---

## 5. Development Tools
- **IDE**: IntelliJ IDEA
- **Version Control**: Git / GitHub

---

## 6. AI Agent
- **Tool**: Claude Code

활용 전략은 [agent-usage.md](agent-usage.md) 참고.

---

## 7. 시스템 아키텍처

런타임 흐름과 계층별 역할은 [architecture.md](architecture.md) 참고.

---

## 향후 확장 고려
- Web Dashboard (Spring MVC 또는 React)
- Redis (캐싱 및 세션 관리)
- 메시지 큐 (Kafka / RabbitMQ)
- 모니터링 (Prometheus, Grafana)
