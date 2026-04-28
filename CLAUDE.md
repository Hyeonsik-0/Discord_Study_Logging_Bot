# studybot

Spring Boot + JDA Discord study-logging bot.

## Tech Stack
- Java 17 / Spring Boot / Gradle
- JDA (Java Discord API)
- MySQL + Spring Data JPA
- Docker / Docker Compose

## Architecture
See [`docs/architecture.md`](docs/architecture.md).

## Prohibited
- Field injection (`@Autowired` on fields)
- Business logic inside listeners
- Over-engineering before MVP is complete
- Obvious/redundant comments that repeat the code

## Layer guides
- [`listener/CLAUDE.md`](src/main/java/com/hyeonsik/studybot/listener/CLAUDE.md) — JDA event edge cases
- [`service/CLAUDE.md`](src/main/java/com/hyeonsik/studybot/service/CLAUDE.md) — TDD, timezone, session invariants
