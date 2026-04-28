package com.hyeonsik.studybot.repository;

import com.hyeonsik.studybot.domain.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUserIdAndStartTimeBetween(long userId, LocalDateTime from, LocalDateTime to);

    List<StudySession> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);

    List<StudySession> findByEndTimeIsNull();

    Optional<StudySession> findByUserIdAndEndTimeIsNull(long userId);
}
