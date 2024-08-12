-- 역할 삽입
INSERT INTO roles(type, name) VALUES('TEAM', 'HEAD');
INSERT INTO roles(type, name) VALUES('TEAM', 'LEADER');
INSERT INTO roles(type, name) VALUES('TEAM', 'MATE');

-- 사용자 삽입 (테스트 시 사용되는 계정 비밀번호는 12345678으로 모두 동일)
INSERT INTO users (email, password, provider, nickname, bio, image, password_change_date, password_failure_count) VALUES
    ('brown@example.com', '{noop}12345678', 'LOCAL', '브라운', '게임을 좋아하는 개발자', 'Image1', '2024-01-01 00:00:00', 0),
    ('cony@example.com', '{noop}12345678', 'KAKAO', '코니', '커피와 책을 사랑하는 디자이너', 'Image2', '2024-01-02 00:00:00', 0),
    ('leonard@example.com', '{noop}12345678', 'GOOGLE', '레너드', '자연을 사랑하는 사진작가', 'Image3', '2024-01-03 00:00:00', 0);

-- 팀 삽입
INSERT INTO teams (creator_id, name, content) VALUES
    (1, '알파 팀', '알파 팀은 최고의 기술력을 가진 개발자들로 구성되어 있습니다.'),
    (2, '베타 팀', '베타 팀은 창의적인 디자인을 추구하는 팀입니다.');

-- 팀 사용자 삽입
INSERT INTO team_users (team_id, user_id, role_id) VALUES
    (1, 1, 1), -- 브라운은 알파 팀의 팀장이다
    (1, 2, 3), -- 코니는 알파 팀의 멤버이다
    (2, 3, 2); -- 레너드는 베타 팀의 리더이다

-- 팀 초대 삽입
INSERT INTO team_invitations (team_id, user_id) VALUES
    (1, 3), -- 레너드에게 알파 팀 가입을 초대
    (2, 1); -- 브라운에게 베타 팀 가입을 초대
