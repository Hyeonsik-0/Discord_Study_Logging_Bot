package com.hyeonsik.studybot.listener;

import com.hyeonsik.studybot.service.PingService;
import com.hyeonsik.studybot.service.StudySessionService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class SlashCommandListener extends ListenerAdapter {

    private final PingService pingService;
    private final StudySessionService studySessionService;

    public SlashCommandListener(PingService pingService, StudySessionService studySessionService) {
        this.pingService = pingService;
        this.studySessionService = studySessionService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping" -> event.reply(pingService.ping()).queue();
            case "status" -> handleStatus(event);
            case "help" -> handleHelp(event);
        }
    }

    private void handleStatus(SlashCommandInteractionEvent event) {
        long userId = event.getUser().getIdLong();
        boolean studying = studySessionService.isStudying(userId);
        String message = studying ? "현재 학습 중입니다." : "현재 학습 중이 아닙니다.";
        event.reply(message).queue();
    }

    private void handleHelp(SlashCommandInteractionEvent event) {
        String message = """
                **명령어 목록**
                `/ping` - 봇 연결 상태 확인
                `/status` - 현재 학습 중 여부 확인
                `/today` - 오늘 학습 시간 조회
                `/week` - 이번 주 학습 시간 조회
                `/ranking` - 이번 주 서버 내 학습 순위 조회
                """;
        event.reply(message).queue();
    }
}
