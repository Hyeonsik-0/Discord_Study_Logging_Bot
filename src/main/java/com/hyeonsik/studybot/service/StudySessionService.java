package com.hyeonsik.studybot.service;

import com.hyeonsik.studybot.domain.StudySession;
import com.hyeonsik.studybot.repository.StudySessionRepository;
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

    private final StudySessionRepository studySessionRepository;

    public StudySessionService(StudySessionRepository studySessionRepository) {
        this.studySessionRepository = studySessionRepository;
    }

    public void startSession(long userId) {
        activeSessions.put(userId, LocalDateTime.now());
    }

    public Optional<Duration> endSession(long userId) {
        LocalDateTime startTime = activeSessions.remove(userId);
        if (startTime == null) {
            return Optional.empty();
        }
        LocalDateTime endTime = LocalDateTime.now();
        studySessionRepository.save(new StudySession(userId, startTime, endTime));
        return Optional.of(Duration.between(startTime, endTime));
    }

    public boolean isStudying(long userId) {
        return activeSessions.containsKey(userId);
    }
}
