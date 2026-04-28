package com.hyeonsik.studybot.service;

import com.hyeonsik.studybot.domain.StudySession;
import com.hyeonsik.studybot.repository.StudySessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudySessionService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final Map<Long, LocalDateTime> activeSessions = new ConcurrentHashMap<>();

    private final StudySessionRepository studySessionRepository;

    public StudySessionService(StudySessionRepository studySessionRepository) {
        this.studySessionRepository = studySessionRepository;
    }

    @Transactional
    public void startSession(long userId) {
        LocalDateTime now = LocalDateTime.now(SEOUL);
        if (activeSessions.putIfAbsent(userId, now) == null) {
            studySessionRepository.save(new StudySession(userId, now));
        }
    }

    @Transactional
    public Optional<Duration> endSession(long userId) {
        LocalDateTime startTime = activeSessions.remove(userId);
        if (startTime == null) {
            return Optional.empty();
        }
        LocalDateTime endTime = LocalDateTime.now(SEOUL);
        studySessionRepository.findByUserIdAndEndTimeIsNull(userId)
                .ifPresent(session -> {
                    session.complete(endTime);
                    studySessionRepository.save(session);
                });
        return Optional.of(Duration.between(startTime, endTime));
    }

    public boolean isStudying(long userId) {
        return activeSessions.containsKey(userId);
    }

    // Restores in-progress sessions from DB after bot restart.
    public void restoreActiveSessions() {
        List<StudySession> sessions = studySessionRepository.findByEndTimeIsNull();
        sessions.forEach(s -> activeSessions.putIfAbsent(s.getUserId(), s.getStartTime()));
    }
}
