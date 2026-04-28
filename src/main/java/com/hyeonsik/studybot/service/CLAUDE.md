# Service Layer

All business logic lives here. Services must work in unit tests with no Discord runtime present.

## TDD
- Write tests before implementing service methods when feasible.
- Each service method must have at least one meaningful test.
- Use `@ExtendWith(MockitoExtension.class)` and mock the repository. Do NOT use `@SpringBootTest` for service-layer tests.

## Timezone
All `LocalDateTime` values use `ZoneId.of("Asia/Seoul")`. Store as `static final SEOUL`. Never call `LocalDateTime.now()` without a zone argument.

## Session state invariant (StudySessionService)
Active sessions are tracked in two places simultaneously:
1. `activeSessions` — in-memory `ConcurrentHashMap<Long, LocalDateTime>` for fast lookups
2. `study_sessions` DB table — for persistence across restarts

`startSession` uses `putIfAbsent` so a double-call from a channel move does not overwrite a session or duplicate a DB row. Do not break this dual-write pattern.

## Transactional boundaries
`startSession` and `endSession` are `@Transactional`. Read-only query methods do not need it.

## Comments
Add comments only for non-obvious decisions: time boundary calculations, edge cases, concurrency intent. TODO comments must include a concrete next action.
