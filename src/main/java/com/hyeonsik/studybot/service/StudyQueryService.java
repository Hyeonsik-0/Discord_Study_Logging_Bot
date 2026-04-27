package com.hyeonsik.studybot.service;

import com.hyeonsik.studybot.domain.StudySession;
import com.hyeonsik.studybot.repository.StudySessionRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyQueryService {

    private final StudySessionRepository studySessionRepository;

    public StudyQueryService(StudySessionRepository studySessionRepository) {
        this.studySessionRepository = studySessionRepository;
    }

    public Duration getTodayDuration(long userId) {
        LocalDateTime from = LocalDate.now().atStartOfDay();
        LocalDateTime to = from.plusDays(1);
        List<StudySession> sessions = studySessionRepository.findByUserIdAndStartTimeBetween(userId, from, to);
        return sumDurations(sessions);
    }

    public Duration getWeekDuration(long userId) {
        LocalDateTime from = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime to = LocalDate.now().plusDays(1).atStartOfDay();
        List<StudySession> sessions = studySessionRepository.findByUserIdAndStartTimeBetween(userId, from, to);
        return sumDurations(sessions);
    }

    public List<RankingEntry> getWeekRanking() {
        LocalDateTime from = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime to = LocalDate.now().plusDays(1).atStartOfDay();
        List<StudySession> sessions = studySessionRepository.findByStartTimeBetween(from, to);

        return sessions.stream()
                .collect(Collectors.groupingBy(
                        StudySession::getUserId,
                        Collectors.reducing(Duration.ZERO,
                                s -> Duration.between(s.getStartTime(), s.getEndTime()),
                                Duration::plus)
                ))
                .entrySet().stream()
                .map(e -> new RankingEntry(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(RankingEntry::totalDuration).reversed())
                .toList();
    }

    private Duration sumDurations(List<StudySession> sessions) {
        return sessions.stream()
                .map(s -> Duration.between(s.getStartTime(), s.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    public record RankingEntry(long userId, Duration totalDuration) {}
}
