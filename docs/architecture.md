# Architecture

Layered architecture, JDA event-driven inputs, JPA-backed persistence.

## Layer Flow

```
Discord Gateway → JDA Event Listener → Service → Repository → MySQL
```

## Layer Responsibilities

| Layer | Responsibility |
|---|---|
| Listener (JDA) | Receive Discord events, extract user/channel IDs, delegate to services. Input adapter only. |
| Service | All business logic — session lifecycle, time aggregation, ranking. Must run without Discord runtime. |
| Repository | Spring Data JPA interfaces. Derived queries; no custom logic. |
| Domain | JPA entities representing core data (e.g., `StudySession`). |

## Core Principles

- Listeners stay thin — no business logic, no DB access, no time calculations.
- Services contain all business logic and must be unit-testable without Discord.
- Constructor injection only; no `@Autowired` field injection.
- `@Transactional` on all mutating service methods.
- Read-only query methods do not need `@Transactional`.

## Event Flows

### Slash Command
```
User command → JDA Listener → Service → Repository → Response to user
```

### Voice Channel Event
```
Voice join/leave → VoiceEventListener → StudySessionService → study_sessions table
```

### Bot Restart Recovery
On `ApplicationReadyEvent`, restore unfinished sessions from DB, then scan currently occupied study channels and start sessions for present members.
