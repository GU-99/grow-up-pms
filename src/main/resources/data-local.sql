-- 역할 삽입
INSERT INTO roles(type, name) VALUES('TEAM', 'HEAD');    -- 관리자
INSERT INTO roles(type, name) VALUES('TEAM', 'LEADER');  -- 리더
INSERT INTO roles(type, name) VALUES('TEAM', 'MATE');    -- 몜버

INSERT INTO roles(type, name) VALUES('PROJECT', 'ADMIN');    -- 관리자
INSERT INTO roles(type, name) VALUES('PROJECT', 'LEADER');   -- 리더
INSERT INTO roles(type, name) VALUES('PROJECT', 'ASSIGNEE'); -- 수행자
INSERT INTO roles(type, name) VALUES('PROJECT', 'VIEWER');   -- 이 외(읽기 전용)

-- 사용자 삽입 (테스트 시 사용되는 계정 비밀번호는 test1234!로 모두 동일)
INSERT INTO users (username, password, email, provider, nickname, bio, image, password_change_date, password_failure_count) VALUES
    ('brown', '{noop}test1234!', 'brown@example.com', 'LOCAL', '브라운', '게임을 좋아하는 개발자', 'Image1', '2024-01-01 00:00:00', 0),
    ('cony', '{noop}test1234!', 'cony@example.com', 'KAKAO', '코니', '커피와 책을 사랑하는 디자이너', 'Image2', '2024-01-02 00:00:00', 0),
    ('leonard', '{noop}test1234!', 'leonard@example.com', 'GOOGLE', '레너드', '자연을 사랑하는 사진작가', 'Image3', '2024-01-03 00:00:00', 0),
    ('sally', '{noop}test1234!', 'sally@example.com', 'LOCAL', '샐리', '24시간이 모자란 워커홀릭 개발자', 'Image1', '2024-01-01 00:00:00', 0),
    ('james', '{noop}test1234!', 'james@example.com', 'KAKAO', '제임스', '커피를 코드로 바꾸는 마법사', 'Image2', '2024-01-02 00:00:00', 0),
    ('edward', '{noop}test1234!', 'edward@example.com', 'GOOGLE', '에드워드', '버그를 춤추게 하는 디버깅의 달인', 'Image3', '2024-01-03 00:00:00', 0),
    ('mary', '{noop}test1234!', 'mary@example.com', 'LOCAL', '메리', '픽셀을 요리하는 디자인 셰프', 'Image4', '2024-01-04 00:00:00', 0),
    ('tom', '{noop}test1234!', 'tom@example.com', 'LOCAL', '톰', '알고리즘으로 세상을 정복하려는 꿈나무', 'Image5', '2024-01-05 00:00:00', 0);

-- 팀 삽입
INSERT INTO teams (creator_id, name, content) VALUES
    (1, '야근코딩단', '야근은 우리의 열정을 증명할 뿐. 우리의 코드는 영원히 살아있다!'),
    (2, '클라우드구름이', '클라우드 기술로 IT 세상을 떠받치는 믿음직한 개발자들의 모임'),
    (3, '님아코드바다', '버그가 넘실대는 코드의 바다를 헤엄치는 용감한 개발자들'),
    (4, '아이디어불쇼', '번뜩이는 아이디어로 프로젝트에 불을 지피는 창의력의 대가들'),
    (5, '환생컴퍼니', '레거시 코드도 우리 손에 걸리면 화려하게 환생한다!');

-- 팀 사용자 삽입
INSERT INTO team_users (team_id, user_id, role_id, is_pending_approval) VALUES
    (1, 1, 1, false),   -- 브라운은 야근코딩단의 HEAD
    (2, 2, 1, false),   -- 코니는 클라우드구름이의 HEAD
    (1, 2, 3, false),   -- 코니는 야근코딩단의 MATE
    (4, 2, 2, true),    -- 코니는 아이디어불쇼의 LEADER (가입 대기 중)
    (3, 3, 1, false),   -- 레너드는 님아코드바다의 HEAD
    (2, 3, 2, true),    -- 레너드는 클라우드구름이의 LEADER (가입 대기 중)
    (5, 3, 3, false),   -- 레너드는 환생컴퍼니의 MATE
    (4, 4, 1, false),   -- 샐리는 아이디어불쇼의 HEAD
    (2, 4, 3, true),    -- 샐리는 클라우드구름이의 MATE (가입 대기 중)
    (5, 5, 1, false),   -- 제임스는 환생컴퍼니의 HEAD
    (4, 5, 3, false);   -- 제임스는 아이디어불쇼의 MATE

-- 권한 삽입
INSERT INTO permissions (name) VALUES ('TEAM_DELETE');
INSERT INTO permissions (name) VALUES ('TEAM_UPDATE');
INSERT INTO permissions (name) VALUES ('TEAM_KICK_MEMBER');
INSERT INTO permissions (name) VALUES ('TEAM_MEMBER_ROLE_UPDATE');
INSERT INTO permissions (name) VALUES ('TEAM_INVITE_MEMBER');

INSERT INTO permissions (name) VALUES ('PROJECT_STATUS_READ');
INSERT INTO permissions (name) VALUES ('PROJECT_STATUS_WRITE');
INSERT INTO permissions (name) VALUES ('PROJECT_STATUS_DELETE');
INSERT INTO permissions (name) VALUES ('PROJECT_TASK_READ');
INSERT INTO permissions (name) VALUES ('PROJECT_TASK_WRITE');
INSERT INTO permissions (name) VALUES ('PROJECT_TASK_DELETE');

-- TEAM_HEAD 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1); -- TEAM_DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2); -- TEAM_UPDATE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 3); -- TEAM_KICK_MEMBER
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 4); -- TEAM_MEMBER_ROLE_UPDATE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 5); -- TEAM_INVITE_MEMBER

-- TEAM_LEADER 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 3); -- TEAM_KICK_MEMBER

-- PROJECT_ADMIN 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 6); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 7); -- PROJECT_STATUS_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 8); -- PROJECT_STATUS_DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 9); -- PROJECT_TASK_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 10); -- PROJECT_TASK_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 11); -- PROJECT_TASK_DELETE

-- PROJECT_LEADER 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 6); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 7); -- PROJECT_STATUS_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 8); -- PROJECT_STATUS_DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 9); -- PROJECT_TASK_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 10); -- PROJECT_TASK_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 11); -- PROJECT_TASK_DELETE

-- PROJECT_ASSIGNEE 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 6); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 9); -- PROJECT_TASK_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 10); -- PROJECT_TASK_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 11); -- PROJECT_TASK_DELETE

-- PROJECT_VIEWER 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (7, 6); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (7, 9); -- PROJECT_TASK_READ
