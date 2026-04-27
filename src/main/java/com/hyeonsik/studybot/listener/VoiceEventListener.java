package com.hyeonsik.studybot.listener;

import com.hyeonsik.studybot.service.StudySessionService;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class VoiceEventListener extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(VoiceEventListener.class);

    private final StudySessionService studySessionService;

    public VoiceEventListener(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        long userId = event.getMember().getIdLong();

        // When a member moves between channels, both getChannelLeft() and getChannelJoined() are non-null.
        // Only pure join (no previous channel) or pure leave (no next channel) triggers session changes.
        boolean joined = event.getChannelLeft() == null;
        boolean left = event.getChannelJoined() == null;

        if (joined) {
            studySessionService.startSession(userId);
        } else if (left) {
            Optional<Duration> duration = studySessionService.endSession(userId);
            duration.ifPresent(d -> log.info("학습 종료 - {} {}분 {}초",
                    event.getMember().getEffectiveName(), d.toMinutes(), d.toSecondsPart()));
        }
    }
}
