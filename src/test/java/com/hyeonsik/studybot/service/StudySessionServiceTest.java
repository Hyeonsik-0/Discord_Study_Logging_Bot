package com.hyeonsik.studybot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StudySessionServiceTest {

    private StudySessionService studySessionService;

    @BeforeEach
    void setUp() {
        studySessionService = new StudySessionService();
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
    void endSession_withNoActiveSession_shouldReturnEmpty() {
        Optional<Duration> duration = studySessionService.endSession(99999L);

        assertThat(duration).isEmpty();
    }
}
