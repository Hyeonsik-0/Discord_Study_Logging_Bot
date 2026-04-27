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

## Learning Goal

The goal is not only to build a working bot, but to:

- Improve Java + Spring Boot proficiency
- Understand backend system design
- Practice clean architecture and testable code
- Learn how to effectively use AI in development