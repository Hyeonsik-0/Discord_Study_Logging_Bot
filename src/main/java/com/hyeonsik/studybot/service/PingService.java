package com.hyeonsik.studybot.service;

import org.springframework.stereotype.Service;

@Service
public class PingService {

    public String ping() {
        return "Pong!";
    }
}
