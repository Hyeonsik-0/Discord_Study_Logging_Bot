package com.hyeonsik.studybot.service;

import com.hyeonsik.studybot.repository.StudySessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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
    void endSession_shouldReturnDurationAndStopTracking() {
        studySessionService.startSession(12345L);

        Optional<Duration> duration = studySessionService.endSession(12345L);

        assertThat(duration).isPresent();
        assertThat(studySessionService.isStudying(12345L)).isFalse();
    }

    @Test
    void endSession_shouldSaveSessionToRepository() {
        studySessionService.startSession(12345L);

        studySessionService.endSession(12345L);

        verify(studySessionRepository).save(any());
    }

    @Test
    void endSession_withNoActiveSession_shouldReturnEmpty() {
        Optional<Duration> duration = studySessionService.endSession(99999L);

        assertThat(duration).isEmpty();
    }
}
