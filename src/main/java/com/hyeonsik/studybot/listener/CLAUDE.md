# Listener Layer

Listeners extend `ListenerAdapter` and must stay thin: extract user/channel IDs, check policy, delegate to a service, return. No business logic here.

## JDA edge cases

### Voice state double-fire (VoiceEventListener)
`onGuildVoiceUpdate` fires once for a channel move with both `channelLeft` and `channelJoined` set. Use the `wasStudy`/`isStudy` booleans to distinguish join, leave, and channel-move. A channel move must not start or end a session.

### Bot restart recovery (BotStartupHandler)
On `ApplicationReadyEvent`, call `studySessionService.restoreActiveSessions()` first, then scan active voice channels. Use `vc.getMembers()` — do NOT use `guild.getMembers()` because `GUILD_MEMBERS` intent is disabled.

### Study channel detection
Channel eligibility is determined solely by `StudyChannelPolicy.isStudyChannel()`. Do not hard-code channel names or IDs in listeners.

## What NOT to add
- No `@Transactional` here — that belongs in services.
- No duration calculations, ranking logic, or DB queries.
