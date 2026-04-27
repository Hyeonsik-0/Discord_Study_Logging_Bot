package com.hyeonsik.studybot.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudySessionService {

    // TODO: Replace with DB-backed session storage to survive bot restarts.
    private final Map<Long, LocalDateTime> activeSessions = new ConcurrentHashMap<>();

    public void startSession(long userId) {
        activeSessions.put(userId, LocalDateTime.now());
    }

    public Optional<Duration> endSession(long userId) {
        LocalDateTime startTime = activeSessions.remove(userId);
        if (startTime == null) {
            return Optional.empty();
        }
        return Optional.of(Duration.between(startTime, LocalDateTime.now()));
    }

    public boolean isStudying(long userId) {
        return activeSessions.containsKey(userId);
    }
}
