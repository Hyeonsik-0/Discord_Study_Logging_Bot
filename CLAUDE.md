# Development Guidelines

This project aims to build a Discord study logging bot while following
conventional Spring Boot backend practices and improving practical development skills.

---

## Spring Development Style

Follow idiomatic Spring Boot conventions as commonly taught in Spring Academy.

### Architecture

Use layered architecture:

Listener (JDA) → Service → Repository → Domain

- Listener: handles Discord events (input layer)
- Service: contains business logic (core)
- Repository: handles database access
- Domain/Entity: represents core data

### Principles

- Keep JDA listeners thin (no business logic inside)
- Place all business logic in the Service layer
- Design services to be testable without Discord runtime
- Use constructor injection (no field injection)
- Use Spring configuration classes for external setup (e.g., JDA)
- Use Spring Data JPA for persistence
- Use transactions where data consistency matters
- Keep code simple, readable, and maintainable
- Avoid over-engineering before MVP is complete

---

## Test Driven Development (TDD)

This project follows a pragmatic TDD approach.

### Guidelines

- Apply TDD primarily to the Service layer
- Write tests before implementing business logic when feasible
- Do not force TDD for simple wiring code (e.g., Listener, Config)
- Each feature should have at least one meaningful test
- Keep tests fast, isolated, and deterministic
- Prefer unit tests over heavy integration tests in early stages

### Testing Scope

- Service Layer: MUST be testable and covered by unit tests
- Repository Layer: minimal testing (Spring Data JPA)
- Listener Layer: test indirectly via Service

### Tools

- JUnit 5
- Spring Boot Test
- H2 (optional for testing)

---

## Development Approach

- Build features incrementally (MVP first)
- Do not generate the entire system at once
- Validate each feature by running the application
- Debug issues using logs and error messages
- Refactor after functionality is confirmed

---

## Claude Usage Guidelines

Claude should act as:

1. Architecture reviewer
2. Code assistant (small units only)
3. Debugging assistant
4. Refactoring advisor

### When generating code:

- Follow Spring Boot conventions
- Respect layered architecture
- Keep code testable
- Avoid unnecessary abstractions
- Explain design decisions when relevant

---

## Commenting Guidelines

- Prefer clear naming and simple structure over excessive comments.
- Do not add obvious comments that merely repeat the code.
- Add comments only when they explain intention, constraints, or non-obvious decisions.
- Use comments for:
  - Discord/JDA-specific behavior
  - Time calculation rules
  - Transaction boundaries
  - Edge cases such as bot restart, duplicate voice events, or channel movement
  - Temporary limitations or TODOs
- Avoid large block comments unless they explain important design decisions.
- Keep comments concise and maintain them when code changes.

### JavaDoc

- Use JavaDoc for public services or methods only when the behavior is not obvious.
- Do not generate JavaDoc for every class or method automatically.
- Prefer test names and method names to document expected behavior.

### TODO Rule

- TODO comments must include a concrete reason or next action.

Good:
```java
// TODO: Handle unfinished sessions after bot restart.
````

Bad:

```java
// TODO: Fix later.
```

### Examples

Bad:

```java
// Get user id
String userId = event.getUser().getId();
```

Good:

```java
// Discord may emit multiple voiceStateUpdate events during channel movement.
if (isChannelMove(event)) {
    return;
}
```

---

## Learning Goal

The goal is not only to build a working bot, but to:

- Improve Java + Spring Boot proficiency
- Understand backend system design
- Practice clean architecture and testable code
- Learn how to effectively use AI in development