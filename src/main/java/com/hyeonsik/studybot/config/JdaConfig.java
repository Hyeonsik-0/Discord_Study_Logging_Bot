package com.hyeonsik.studybot.config;

import com.hyeonsik.studybot.listener.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdaConfig {

    private final String token;
    private final SlashCommandListener slashCommandListener;

    public JdaConfig(@Value("${discord.token}") String token,
                     SlashCommandListener slashCommandListener) {
        this.token = token;
        this.slashCommandListener = slashCommandListener;
    }

    @Bean
    public JDA jda() throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(slashCommandListener)
                .build()
                .awaitReady();

        jda.updateCommands()
                .addCommands(Commands.slash("ping", "봇 연결 상태 확인"))
                .queue();

        return jda;
    }
}
