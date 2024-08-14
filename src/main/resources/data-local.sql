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
    ('leonard', '{noop}test1234!', 'leonard@example.com', 'GOOGLE', '레너드', '자연을 사랑하는 사진작가', 'Image3', '2024-01-03 00:00:00', 0);

-- 팀 삽입
INSERT INTO teams (creator_id, name, content) VALUES
    (1, '알파 팀', '알파 팀은 최고의 기술력을 가진 개발자들로 구성되어 있습니다.'),
    (2, '베타 팀', '베타 팀은 창의적인 디자인을 추구하는 팀입니다.');

-- 팀 사용자 삽입
INSERT INTO team_users (team_id, user_id, role_id, is_pending_approval) VALUES
    (1, 1, 1, true), -- 브라운은 알파 팀의 팀장이다
    (1, 2, 3, true), -- 코니는 알파 팀의 멤버이다
    (2, 3, 2, true); -- 레너드는 베타 팀의 리더이다

-- 팀 초대 삽입
INSERT INTO team_invitations (team_id, user_id) VALUES
    (1, 3), -- 레너드에게 알파 팀 가입을 초대
    (2, 1); -- 브라운에게 베타 팀 가입을 초대

-- 권한 삽입
INSERT INTO permissions (name) VALUES ('TEAM_DELETE');
INSERT INTO permissions (name) VALUES ('TEAM_UPDATE');
INSERT INTO permissions (name) VALUES ('PROJECT_STATUS_READ');
INSERT INTO permissions (name) VALUES ('PROJECT_STATUS_WRITE');
INSERT INTO permissions (name) VALUES ('PROJECT_STATUS_DELETE');
INSERT INTO permissions (name) VALUES ('PROJECT_TASK_READ');
INSERT INTO permissions (name) VALUES ('PROJECT_TASK_WRITE');
INSERT INTO permissions (name) VALUES ('PROJECT_TASK_DELETE');

-- TEAM_HEAD 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1); -- TEAM_DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2); -- TEAM_UPDATE

-- PROJECT_ADMIN 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 3); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 4); -- PROJECT_STATUS_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 5); -- PROJECT_STATUS_DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 6); -- PROJECT_TASK_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 7); -- PROJECT_TASK_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 8); -- PROJECT_TASK_DELETE

-- PROJECT_LEADER 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 3); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 4); -- PROJECT_STATUS_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 5); -- PROJECT_STATUS_DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 6); -- PROJECT_TASK_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 7); -- PROJECT_TASK_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 8); -- PROJECT_TASK_DELETE

-- PROJECT_ASSIGNEE 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 3); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 6); -- PROJECT_TASK_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 7); -- PROJECT_TASK_WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 8); -- PROJECT_TASK_DELETE

-- PROJECT_VIEWER 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (7, 3); -- PROJECT_STATUS_READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (7, 6); -- PROJECT_TASK_READ
