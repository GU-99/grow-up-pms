package com.growup.pms.test.fixture.user.data;

import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserTestData {

    public static User 에밀리() {
        return 사용자는()
                .닉네임이("Emily")
                .아이디가("emily_j")
                .비밀번호가("bookWorm2024!")
                .이메일이("emily_jung@gu99.com")
                .인증_프로바이더가(Provider.GOOGLE)
                .자기소개가("안녕하세요! 25살 대학원생입니다. 문학과 커피를 사랑합니다.")
                .이다();
    }

    public static User 알렉스() {
        return 사용자는()
            .닉네임이("Alex")
            .아이디가("alex_lee")
            .비밀번호가("fitnessFreak9@")
            .이메일이("alex_lee@gu99.com")
            .인증_프로바이더가(Provider.LOCAL)
            .자기소개가("30대 초반 직장인입니다. 운동과 건강한 라이프스타일에 관심이 많아요.")
            .이다();
    }

    public static User 소피아() {
        return 사용자는()
            .닉네임이("Sophia")
            .아이디가("sophia_p")
            .비밀번호가("artL@ver1234")
            .이메일이("sophia_park@gu99.com")
            .인증_프로바이더가(Provider.KAKAO)
            .자기소개가("28살 프리랜서 디자이너입니다. 미술관 투어와 여행을 좋아해요.")
            .이다();
    }

    public static User 마이클() {
        return 사용자는()
                .닉네임이("Michael")
                .아이디가("mike_kim")
                .비밀번호가("techGuru2023$")
                .이메일이("michael_kim@gu99.com")
                .인증_프로바이더가(Provider.LOCAL)
                .자기소개가("35살 소프트웨어 엔지니어입니다. 새로운 기술과 오픈소스 프로젝트에 관심이 많아요.")
                .이다();
    }

    public static User 올리비아() {
        return 사용자는()
                .닉네임이("Olivia")
                .아이디가("olivia_choi")
                .비밀번호가("yogaLover#88")
                .이메일이("olivia_choi@gu99.com")
                .인증_프로바이더가(Provider.GOOGLE)
                .자기소개가("32살 요가 강사입니다. 명상과 건강한 식단에 관심이 많아요.")
                .이다();
    }

    public static User 데이비드() {
        return 사용자는()
                .닉네임이("David")
                .아이디가("david_song")
                .비밀번호가("musicMan!2024")
                .이메일이("david_song@gu99.com")
                .인증_프로바이더가(Provider.KAKAO)
                .자기소개가("27살 음악 프로듀서입니다. 다양한 장르의 음악을 만들고 공연하는 것을 좋아해요.")
                .이다();
    }

    public static User 에마() {
        return 사용자는()
                .닉네임이("Emma")
                .아이디가("emma_han")
                .비밀번호가("travelBug2023*")
                .이메일이("emma_han@gu99.com")
                .인증_프로바이더가(Provider.LOCAL)
                .자기소개가("29살 여행 블로거입니다. 세계 각국의 문화와 음식을 경험하는 것을 좋아해요.")
                .이다();
    }

    public static User 라이언() {
        return 사용자는()
                .닉네임이("Ryan")
                .아이디가("ryan_park")
                .비밀번호가("gamerPro456!")
                .이메일이("ryan_park@gu99.com")
                .인증_프로바이더가(Provider.GOOGLE)
                .자기소개가("23살 프로 게이머입니다. e스포츠와 스트리밍에 열정을 가지고 있어요.")
                .이다();
    }
}
