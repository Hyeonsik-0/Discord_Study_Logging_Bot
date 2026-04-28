package com.hyeonsik.studybot.service;

import com.hyeonsik.studybot.domain.StudySession;
import com.hyeonsik.studybot.repository.StudySessionRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyQueryService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final StudySessionRepository studySessionRepository;

    public StudyQueryService(StudySessionRepository studySessionRepository) {
        this.studySessionRepository = studySessionRepository;
    }

    public Duration getTodayDuration(long userId) {
        LocalDateTime now = LocalDateTime.now(SEOUL);
        LocalDateTime from = LocalDate.now(SEOUL).atStartOfDay();
        LocalDateTime to = from.plusDays(1);
        List<StudySession> sessions = studySessionRepository.findByUserIdAndStartTimeBetween(userId, from, to);
        return sumDurations(sessions, now);
    }

    public Duration getWeekDuration(long userId) {
        LocalDateTime now = LocalDateTime.now(SEOUL);
        LocalDateTime from = LocalDate.now(SEOUL).with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime to = LocalDate.now(SEOUL).plusDays(1).atStartOfDay();
        List<StudySession> sessions = studySessionRepository.findByUserIdAndStartTimeBetween(userId, from, to);
        return sumDurations(sessions, now);
    }

    public List<RankingEntry> getWeekRanking() {
        LocalDateTime now = LocalDateTime.now(SEOUL);
        LocalDateTime from = LocalDate.now(SEOUL).with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime to = LocalDate.now(SEOUL).plusDays(1).atStartOfDay();
        List<StudySession> sessions = studySessionRepository.findByStartTimeBetween(from, to);

        return sessions.stream()
                .collect(Collectors.groupingBy(
                        StudySession::getUserId,
                        Collectors.reducing(Duration.ZERO,
                                s -> Duration.between(s.getStartTime(),
                                        s.getEndTime() != null ? s.getEndTime() : now),
                                Duration::plus)
                ))
                .entrySet().stream()
                .map(e -> new RankingEntry(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(RankingEntry::totalDuration).reversed())
                .toList();
    }

    private Duration sumDurations(List<StudySession> sessions, LocalDateTime now) {
        return sessions.stream()
                .map(s -> Duration.between(s.getStartTime(),
                        s.getEndTime() != null ? s.getEndTime() : now))
                .reduce(Duration.ZERO, Duration::plus);
    }

    public record RankingEntry(long userId, Duration totalDuration) {}
}
