package com.hyeonsik.studybot.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudyChannelPolicyTest {

    private final StudyChannelPolicy policy = new StudyChannelPolicy("공부방");

    @Test
    void isStudyChannel_withMatchingPrefix_returnsTrue() {
        assertThat(policy.isStudyChannel("공부방1")).isTrue();
        assertThat(policy.isStudyChannel("공부방-오전")).isTrue();
        assertThat(policy.isStudyChannel("공부방A")).isTrue();
    }

    @Test
    void isStudyChannel_withExactPrefix_returnsTrue() {
        assertThat(policy.isStudyChannel("공부방")).isTrue();
    }

    @Test
    void isStudyChannel_withNonMatchingName_returnsFalse() {
        assertThat(policy.isStudyChannel("일반채널")).isFalse();
        assertThat(policy.isStudyChannel("잡담방")).isFalse();
    }

    @Test
    void isStudyChannel_withEmptyName_returnsFalse() {
        assertThat(policy.isStudyChannel("")).isFalse();
    }
}
