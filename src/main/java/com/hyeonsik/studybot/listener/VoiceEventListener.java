package com.hyeonsik.studybot.listener;

import com.hyeonsik.studybot.config.StudyChannelPolicy;
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
    private final StudyChannelPolicy studyChannelPolicy;

    public VoiceEventListener(StudySessionService studySessionService, StudyChannelPolicy studyChannelPolicy) {
        this.studySessionService = studySessionService;
        this.studyChannelPolicy = studyChannelPolicy;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        long userId = event.getMember().getIdLong();

        boolean wasStudy = event.getChannelLeft() != null
                && studyChannelPolicy.isStudyChannel(event.getChannelLeft().getName());
        boolean isStudy = event.getChannelJoined() != null
                && studyChannelPolicy.isStudyChannel(event.getChannelJoined().getName());

        if (!wasStudy && isStudy) {
            studySessionService.startSession(userId);
        } else if (wasStudy && !isStudy) {
            Optional<Duration> duration = studySessionService.endSession(userId);
            duration.ifPresent(d -> log.info("학습 종료 - {} {}분 {}초",
                    event.getMember().getEffectiveName(), d.toMinutes(), d.toSecondsPart()));
        }
    }
}
