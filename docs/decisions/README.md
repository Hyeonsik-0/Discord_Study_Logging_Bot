# Architecture Decision Records (ADR)

큰 기술적 결정을 한 건당 짧은 문서로 남긴다. 6개월 뒤 "왜 이걸 골랐지?" 자문할 때 답이 적혀있도록.

## 언제 쓰는가

다음 중 하나에 해당하면 ADR 후보:

- **라이브러리·프레임워크 선택** — 여러 후보 중 골랐을 때 (e.g., spring-dotenv vs EnvFile 플러그인)
- **아키텍처 변경** — 새 계층 추가, 기존 계층 분리/병합
- **데이터 모델 변경** — 큰 스키마 변경, 핵심 엔티티 추가/제거
- **외부 의존성 도입** — 새 인프라 (Redis, MQ, 모니터링 등)
- **명시적 trade-off 결정** — 두 가지 이상 옵션을 저울질하고 한 쪽으로 결정한 모든 경우
- **되돌리기 어려운 결정** — 마이그레이션 비용이 큰 선택

## 언제 쓰지 않는가

- 버그 수정
- 작은 리팩토링 (함수 추출, 변수명 변경)
- 코드 스타일 결정
- 단일 함수·클래스 추가
- 자명한 결정 (대안이 없거나 명백한 경우)

기준: **"이 결정을 모르고 코드만 봤을 때 의도를 알 수 있는가?"** 알 수 있으면 ADR 불필요.

## 작성 규칙

### 파일명
`NNNN-짧은-제목.md` (4자리 zero-padded, kebab-case)

예) `0001-spring-dotenv.md`, `0012-redis-session-cache.md`

### 양식 (MADR 단축)

```markdown
# NNNN. 제목

- 상태: Proposed | Accepted | Deprecated | Superseded by NNNN
- 일자: YYYY-MM-DD

## 맥락 (Context)
어떤 상황에서 결정이 필요해졌는가. 제약사항·요구사항.

## 결정 (Decision)
무엇을 하기로 했는가. 한두 줄.

## 대안 (Alternatives Considered)
검토했지만 채택하지 않은 옵션들. 각각 장단점 짧게.

## 결과 (Consequences)
이 결정으로 얻는 것·잃는 것·따라오는 작업.
```

### 상태 (Status)

- **Proposed** — 제안만 한 단계
- **Accepted** — 채택, 적용 중
- **Deprecated** — 더 이상 권장 안 하지만 그대로 둠
- **Superseded by NNNN** — 다른 ADR 로 대체됨 (원본은 그대로 두고 새 ADR 작성)

**한번 Accepted 된 ADR 은 수정하지 않는다.** 결정이 바뀌면 새 ADR 작성 후 원본을 Superseded 처리.

## 인덱스

| # | 제목 | 상태 |
|---|---|---|
| [0001](0001-spring-dotenv.md) | Discord 토큰을 .env 파일로 관리 | Accepted |
