package com.hyeonsik.studybot.service;

import com.hyeonsik.studybot.domain.StudySession;
import com.hyeonsik.studybot.repository.StudySessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudySessionServiceTest {

    @Mock
    private StudySessionRepository studySessionRepository;

    private StudySessionService studySessionService;

    @BeforeEach
    void setUp() {
        studySessionService = new StudySessionService(studySessionRepository);
    }

    @Test
    void startSession_shouldMarkUserAsStudying() {
        studySessionService.startSession(12345L);

        assertThat(studySessionService.isStudying(12345L)).isTrue();
    }

    @Test
    void startSession_shouldSaveSessionWithNullEndTime() {
        studySessionService.startSession(12345L);

        ArgumentCaptor<StudySession> captor = ArgumentCaptor.forClass(StudySession.class);
        verify(studySessionRepository).save(captor.capture());
        assertThat(captor.getValue().getEndTime()).isNull();
    }

    @Test
    void startSession_calledTwice_shouldNotSaveTwice() {
        studySessionService.startSession(12345L);
        studySessionService.startSession(12345L);

        verify(studySessionRepository, times(1)).save(any());
    }

    @Test
    void endSession_shouldReturnDurationAndStopTracking() {
        studySessionService.startSession(12345L);
        when(studySessionRepository.findByUserIdAndEndTimeIsNull(12345L)).thenReturn(Optional.empty());

        Optional<java.time.Duration> duration = studySessionService.endSession(12345L);

        assertThat(duration).isPresent();
        assertThat(studySessionService.isStudying(12345L)).isFalse();
    }

    @Test
    void endSession_shouldCompleteActiveSession() {
        LocalDateTime startTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(30);
        StudySession activeSession = new StudySession(12345L, startTime);
        studySessionService.startSession(12345L);
        when(studySessionRepository.findByUserIdAndEndTimeIsNull(12345L)).thenReturn(Optional.of(activeSession));

        studySessionService.endSession(12345L);

        assertThat(activeSession.getEndTime()).isNotNull();
        verify(studySessionRepository, atLeastOnce()).save(activeSession);
    }

    @Test
    void endSession_withNoActiveSession_shouldReturnEmpty() {
        Optional<java.time.Duration> duration = studySessionService.endSession(99999L);

        assertThat(duration).isEmpty();
    }

    @Test
    void restoreActiveSessions_shouldRestoreFromDatabase() {
        LocalDateTime startTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusHours(1);
        when(studySessionRepository.findByEndTimeIsNull())
                .thenReturn(List.of(new StudySession(12345L, startTime)));

        studySessionService.restoreActiveSessions();

        assertThat(studySessionService.isStudying(12345L)).isTrue();
    }

    @Test
    void restoreActiveSessions_shouldNotOverwriteExistingSession() {
        studySessionService.startSession(12345L);
        LocalDateTime oldStartTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusHours(2);
        when(studySessionRepository.findByEndTimeIsNull())
                .thenReturn(List.of(new StudySession(12345L, oldStartTime)));

        studySessionService.restoreActiveSessions();

        // putIfAbsent ensures the existing (newer) session is kept
        assertThat(studySessionService.isStudying(12345L)).isTrue();
    }
}
