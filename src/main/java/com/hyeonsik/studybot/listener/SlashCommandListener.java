package com.hyeonsik.studybot.listener;

import com.hyeonsik.studybot.service.PingService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class SlashCommandListener extends ListenerAdapter {

    private final PingService pingService;

    public SlashCommandListener(PingService pingService) {
        this.pingService = pingService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.reply(pingService.ping()).queue();
        }
    }
}
