package com.hyeonsik.studybot.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_sessions", indexes = {
        @Index(name = "idx_user_start", columnList = "userId, startTime"),
        @Index(name = "idx_start_time", columnList = "startTime")
})
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    protected StudySession() {}

    public StudySession(long userId, LocalDateTime startTime) {
        this.userId = userId;
        this.startTime = startTime;
    }

    public StudySession(long userId, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Called when a study session ends.
    public void complete(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getId() { return id; }
    public long getUserId() { return userId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
