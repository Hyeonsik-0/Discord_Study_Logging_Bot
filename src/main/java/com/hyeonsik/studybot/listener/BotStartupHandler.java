package com.hyeonsik.studybot.listener;

import com.hyeonsik.studybot.config.StudyChannelPolicy;
import com.hyeonsik.studybot.service.StudySessionService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BotStartupHandler {

    private static final Logger log = LoggerFactory.getLogger(BotStartupHandler.class);

    private final StudySessionService studySessionService;
    private final StudyChannelPolicy studyChannelPolicy;
    private final JDA jda;

    public BotStartupHandler(StudySessionService studySessionService,
                             StudyChannelPolicy studyChannelPolicy,
                             JDA jda) {
        this.studySessionService = studySessionService;
        this.studyChannelPolicy = studyChannelPolicy;
        this.jda = jda;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        studySessionService.restoreActiveSessions();
        scanStudyChannels();
    }

    // Members in study channels at startup (e.g. after bot restart) are registered as active sessions.
    // Uses voice channel member list instead of guild.getMembers() — GUILD_MEMBERS intent is not enabled.
    private void scanStudyChannels() {
        for (Guild guild : jda.getGuilds()) {
            for (VoiceChannel vc : guild.getVoiceChannels()) {
                if (!studyChannelPolicy.isStudyChannel(vc.getName())) {
                    continue;
                }
                vc.getMembers().forEach(member -> {
                    studySessionService.startSession(member.getIdLong());
                    log.info("봇 재시작 감지 - 스터디 채널 입장 중: {}", member.getEffectiveName());
                });
            }
        }
    }
}
