package com.growup.pms.common.util;

import java.util.concurrent.ThreadLocalRandom;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class NicknameUtil {

    private final String[] ADJECTIVES = {
            "간편한", "건강한", "고급스런", "공정한", "급한", "깨끗한", "끈기있는", "달콤한", "단정한", "따뜻한",
            "똑똑한", "멋진", "미묘한", "밝은", "빠른", "소중한", "신선한", "아름다운", "안전한", "완벽한",
            "기쁜", "즐거운", "혁신적인", "창의적인", "친절한", "용감한", "재미있는", "부드러운", "정직한", "깨달은",
            "넉넉한", "행복한", "유능한", "경쾌한", "신비로운", "유쾌한", "명랑한", "긍정적인", "정중한", "상냥한"
    };

    private final String[] NOUNS = {
            "장미", "튤립", "백합", "국화", "진달래", "나팔꽃", "목련", "봉숭아", "제비꽃", "해바라기", "할미꽃",
            "무궁화", "벚꽃", "개나리", "매화", "산수유", "민들레", "메리골드", "라일락", "아이비", "옥잠화", "감나무",
            "살구나무", "포도나무", "수국", "아마란스", "코스모스", "철쭉", "소나무", "느티나무", "자목련",
            "데이지", "까마중", "산딸기", "비비추", "장미", "튤립", "포플러", "등나무", "마가렛"
    };

    public String generateNickname() {
        String adjective = ADJECTIVES[ThreadLocalRandom.current().nextInt(ADJECTIVES.length)];
        String noun = NOUNS[ThreadLocalRandom.current().nextInt(NOUNS.length)];
        String randomValue = String.valueOf(ThreadLocalRandom.current().nextInt(99999));

        return adjective + noun + randomValue;
    }
}
