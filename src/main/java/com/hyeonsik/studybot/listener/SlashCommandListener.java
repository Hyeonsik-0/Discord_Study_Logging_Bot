package com.hyeonsik.studybot.listener;

import com.hyeonsik.studybot.service.PingService;
import com.hyeonsik.studybot.service.StudyQueryService;
import com.hyeonsik.studybot.service.StudyQueryService.RankingEntry;
import com.hyeonsik.studybot.service.StudySessionService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class SlashCommandListener extends ListenerAdapter {

    private final PingService pingService;
    private final StudySessionService studySessionService;
    private final StudyQueryService studyQueryService;

    public SlashCommandListener(PingService pingService,
                                StudySessionService studySessionService,
                                StudyQueryService studyQueryService) {
        this.pingService = pingService;
        this.studySessionService = studySessionService;
        this.studyQueryService = studyQueryService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping"    -> event.reply(pingService.ping()).queue();
            case "status"  -> handleStatus(event);
            case "today"   -> handleToday(event);
            case "week"    -> handleWeek(event);
            case "ranking" -> handleRanking(event);
            case "help"    -> handleHelp(event);
        }
    }

    private void handleStatus(SlashCommandInteractionEvent event) {
        long userId = event.getUser().getIdLong();
        boolean studying = studySessionService.isStudying(userId);
        event.reply(studying ? "현재 학습 중입니다." : "현재 학습 중이 아닙니다.").queue();
    }

    private void handleToday(SlashCommandInteractionEvent event) {
        long userId = event.getUser().getIdLong();
        Duration duration = studyQueryService.getTodayDuration(userId);
        if (duration.isZero()) {
            event.reply("오늘 학습한 기록이 없습니다.").queue();
            return;
        }
        event.reply("오늘 학습 시간: " + formatDuration(duration)).queue();
    }

    private void handleWeek(SlashCommandInteractionEvent event) {
        long userId = event.getUser().getIdLong();
        Duration duration = studyQueryService.getWeekDuration(userId);
        if (duration.isZero()) {
            event.reply("이번 주 학습한 기록이 없습니다.").queue();
            return;
        }
        event.reply("이번 주 학습 시간: " + formatDuration(duration)).queue();
    }

    private void handleRanking(SlashCommandInteractionEvent event) {
        List<RankingEntry> ranking = studyQueryService.getWeekRanking();
        if (ranking.isEmpty()) {
            event.reply("이번 주 학습 기록이 없습니다.").queue();
            return;
        }
        StringBuilder sb = new StringBuilder("**이번 주 학습 순위**\n");
        for (int i = 0; i < ranking.size(); i++) {
            sb.append(String.format("%d. <@%d> - %s%n",
                    i + 1, ranking.get(i).userId(), formatDuration(ranking.get(i).totalDuration())));
        }
        event.reply(sb.toString()).queue();
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

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        if (hours > 0) {
            return String.format("%d시간 %d분", hours, minutes);
        }
        return String.format("%d분", minutes);
    }
}
