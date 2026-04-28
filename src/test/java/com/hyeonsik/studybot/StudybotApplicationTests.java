package com.hyeonsik.studybot;

import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class StudybotApplicationTests {

	@MockitoBean
	JDA jda;

	@Test
	void contextLoads() {
	}

}
