package com.hyeonsik.studybot.service;

import com.hyeonsik.studybot.domain.StudySession;
import com.hyeonsik.studybot.repository.StudySessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyQueryServiceTest {

    @Mock
    private StudySessionRepository studySessionRepository;

    private StudyQueryService studyQueryService;

    @BeforeEach
    void setUp() {
        studyQueryService = new StudyQueryService(studySessionRepository);
    }

    @Test
    void getTodayDuration_shouldSumAllSessionsForUser() {
        LocalDateTime now = LocalDateTime.now();
        when(studySessionRepository.findByUserIdAndStartTimeBetween(eq(12345L), any(), any()))
                .thenReturn(List.of(
                        new StudySession(12345L, now.minusMinutes(30), now),
                        new StudySession(12345L, now.minusMinutes(20), now.minusMinutes(10))
                ));

        Duration result = studyQueryService.getTodayDuration(12345L);

        assertThat(result).isEqualTo(Duration.ofMinutes(40));
    }

    @Test
    void getTodayDuration_withNoSessions_shouldReturnZero() {
        when(studySessionRepository.findByUserIdAndStartTimeBetween(anyLong(), any(), any()))
                .thenReturn(List.of());

        Duration result = studyQueryService.getTodayDuration(12345L);

        assertThat(result).isZero();
    }

    @Test
    void getWeekRanking_shouldReturnUsersSortedByTotalDuration() {
        LocalDateTime now = LocalDateTime.now();
        when(studySessionRepository.findByStartTimeBetween(any(), any()))
                .thenReturn(List.of(
                        new StudySession(111L, now.minusMinutes(10), now),
                        new StudySession(222L, now.minusMinutes(60), now),
                        new StudySession(111L, now.minusMinutes(20), now.minusMinutes(10))
                ));

        List<StudyQueryService.RankingEntry> ranking = studyQueryService.getWeekRanking();

        assertThat(ranking).hasSize(2);
        assertThat(ranking.get(0).userId()).isEqualTo(222L);
        assertThat(ranking.get(1).userId()).isEqualTo(111L);
    }
}
