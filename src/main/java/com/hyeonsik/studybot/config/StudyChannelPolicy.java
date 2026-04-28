package com.hyeonsik.studybot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StudyChannelPolicy {

    private final String prefix;

    public StudyChannelPolicy(@Value("${study.channel.prefix}") String prefix) {
        this.prefix = prefix;
    }

    public boolean isStudyChannel(String channelName) {
        return channelName.startsWith(prefix);
    }
}
