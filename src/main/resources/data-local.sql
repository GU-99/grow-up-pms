-- 역할 삽입
INSERT INTO roles(type, name) VALUES('TEAM', 'HEAD');    -- 관리자
INSERT INTO roles(type, name) VALUES('TEAM', 'LEADER');  -- 리더
INSERT INTO roles(type, name) VALUES('TEAM', 'MATE');    -- 몜버

INSERT INTO roles(type, name) VALUES('PROJECT', 'ADMIN');    -- 관리자
INSERT INTO roles(type, name) VALUES('PROJECT', 'LEADER');   -- 리더
INSERT INTO roles(type, name) VALUES('PROJECT', 'ASSIGNEE'); -- 수행자

-- 사용자 삽입 (테스트 시 사용되는 계정 비밀번호는 test1234!로 모두 동일)
INSERT INTO users (username, password, email, provider, nickname, bio, image_name, password_change_date, password_failure_count) VALUES
    ('brown', '{noop}test1234!', 'brown@example.com', 'LOCAL', '브라운', '게임을 좋아하는 개발자', 'Image1.jpg', '2024-01-01 00:00:00', 0),
    ('cony', '{noop}test1234!', 'cony@example.com', 'KAKAO', '코니', '커피와 책을 사랑하는 디자이너', 'Image2.png', '2024-01-02 00:00:00', 0),
    ('leonard', '{noop}test1234!', 'leonard@example.com', 'GOOGLE', '레너드', '자연을 사랑하는 사진작가', 'Image3.jpeg', '2024-01-03 00:00:00', 0),
    ('sally', '{noop}test1234!', 'sally@example.com', 'LOCAL', '샐리', '24시간이 모자란 워커홀릭 개발자', 'Image1.webp', '2024-01-01 00:00:00', 0),
    ('james', '{noop}test1234!', 'james@example.com', 'KAKAO', '제임스', '커피를 코드로 바꾸는 마법사', 'Image2.png', '2024-01-02 00:00:00', 0),
    ('edward', '{noop}test1234!', 'edward@example.com', 'GOOGLE', '에드워드', '버그를 춤추게 하는 디버깅의 달인', 'Image3.jpeg', '2024-01-03 00:00:00', 0),
    ('mary', '{noop}test1234!', 'mary@example.com', 'LOCAL', '메리', '픽셀을 요리하는 디자인 셰프', 'Image4.jpg', '2024-01-04 00:00:00', 0),
    ('tom', '{noop}test1234!', 'tom@example.com', 'LOCAL', '톰', '알고리즘으로 세상을 정복하려는 꿈나무', 'Image5.jpeg', '2024-01-05 00:00:00', 0);

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

-- 프로젝트 삽입
INSERT INTO projects (team_id, name, content, start_date, end_date, created_at, updated_at, is_deleted) VALUES
    -- 야근코딩단의 프로젝트들
    (1, '야근 관리 시스템', '야근 시간을 효율적으로 관리하고 기록하는 시스템 개발', '2024-01-01', '2024-03-31', NOW(), NOW(), false),
    (1, '코드 리팩토링 마라톤', '레거시 코드를 현대화하는 대규모 리팩토링 프로젝트', '2024-02-15', '2024-06-30', NOW(), NOW(), false),

    -- 클라우드구름이의 프로젝트들
    (2, '멀티 클라우드 마이그레이션', '기존 시스템을 멀티 클라우드 환경으로 이전', '2024-01-15', '2024-07-31', NOW(), NOW(), false),
    (2, '클라우드 비용 최적화', '클라우드 리소스 사용 효율화 및 비용 절감 프로젝트', '2024-03-01', '2024-05-31', NOW(), NOW(), false),

    -- 님아코드바다의 프로젝트들
    (3, '버그 트래킹 시스템', '실시간 버그 모니터링 및 추적 시스템 개발', '2024-02-01', '2024-04-30', NOW(), NOW(), false),
    (3, '코드 품질 개선', '정적 분석 도구를 활용한 코드 품질 향상 프로젝트', '2024-04-01', '2024-08-31', NOW(), NOW(), false),

    -- 아이디어불쇼의 프로젝트들
    (4, '혁신적 UI/UX 개선', '사용자 경험을 혁신적으로 개선하는 프로젝트', '2024-01-20', '2024-05-31', NOW(), NOW(), false),
    (4, '신규 서비스 프로토타입', '창의적인 신규 서비스 개발 및 프로토타이핑', '2024-03-15', '2024-06-30', NOW(), NOW(), false),

    -- 환생컴퍼니의 프로젝트들
    (5, '레거시 시스템 현대화', '노후화된 시스템의 전면 개편 프로젝트', '2024-02-01', '2024-07-31', NOW(), NOW(), false),
    (5, '마이크로서비스 전환', '모놀리식 아키텍처를 마이크로서비스로 전환', '2024-04-01', '2024-09-30', NOW(), NOW(), false);

-- 프로젝트 사용자 삽입
INSERT INTO project_users (project_id, user_id, role_id, created_at, updated_at, is_deleted) VALUES
    ---- 야근코딩단의 프로젝트들
    -- 야근 관리 시스템 (project_id: 1)
    (1, 1, 4, NOW(), NOW(), false),  -- 브라운: ADMIN
    (1, 2, 5, NOW(), NOW(), false),  -- 코니: LEADER

    -- 코드 리팩토링 마라톤 (project_id: 2)
    (2, 1, 4, NOW(), NOW(), false),  -- 브라운: ADMIN
    (2, 2, 6, NOW(), NOW(), false),  -- 코니: ASSIGNEE

    ---- 클라우드구름이의 프로젝트들
    -- 멀티 클라우드 마이그레이션 (project_id: 3)
    (3, 2, 4, NOW(), NOW(), false),  -- 코니: ADMIN
    (3, 4, 6, NOW(), NOW(), false),  -- 샐리: ASSIGNEE

    -- 클라우드 비용 최적화 (project_id: 4)
    (4, 2, 4, NOW(), NOW(), false),  -- 코니: ADMIN
    (4, 4, 5, NOW(), NOW(), false),  -- 샐리: LEADER

    -- 님아코드바다의 프로젝트들
    -- 버그 트래킹 시스템 (project_id: 5)
    (5, 3, 4, NOW(), NOW(), false),  -- 레너드: ADMIN
    (5, 6, 6, NOW(), NOW(), false),  -- 에드워드: ASSIGNEE
    (5, 7, 6, NOW(), NOW(), false),  -- 메리: ASSIGNEE

    -- 코드 품질 개선 (project_id: 6)
    (6, 3, 4, NOW(), NOW(), false),  -- 레너드: ADMIN
    (6, 6, 5, NOW(), NOW(), false),  -- 에드워드: LEADER
    (6, 8, 6, NOW(), NOW(), false),  -- 톰: ASSIGNEE

    -- 아이디어불쇼의 프로젝트들
    -- 혁신적 UI/UX 개선 (project_id: 7)
    (7, 4, 4, NOW(), NOW(), false),  -- 샐리: ADMIN
    (7, 5, 5, NOW(), NOW(), false),  -- 제임스: LEADER
    (7, 7, 6, NOW(), NOW(), false),  -- 메리: ASSIGNEE

    -- 신규 서비스 프로토타입 (project_id: 8)
    (8, 4, 4, NOW(), NOW(), false),  -- 샐리: ADMIN
    (8, 5, 6, NOW(), NOW(), false),  -- 제임스: ASSIGNEE
    (8, 8, 6, NOW(), NOW(), false),  -- 톰: ASSIGNEE

    -- 환생컴퍼니의 프로젝트들
    -- 레거시 시스템 현대화 (project_id: 9)
    (9, 5, 4, NOW(), NOW(), false),  -- 제임스: ADMIN
    (9, 3, 5, NOW(), NOW(), false),  -- 레너드: LEADER
    (9, 1, 6, NOW(), NOW(), false),  -- 브라운: ASSIGNEE

    -- 마이크로서비스 전환 (project_id: 10)
    (10, 5, 4, NOW(), NOW(), false), -- 제임스: ADMIN
    (10, 3, 6, NOW(), NOW(), false), -- 레너드: ASSIGNEE
    (10, 8, 6, NOW(), NOW(), false); -- 톰: ASSIGNEE

-- 권한 삽입
INSERT INTO permissions (name) VALUES ('TEAM_DELETE_TEAM');
INSERT INTO permissions (name) VALUES ('TEAM_UPDATE_TEAM');
INSERT INTO permissions (name) VALUES ('TEAM_KICK_MEMBER');
INSERT INTO permissions (name) VALUES ('TEAM_UPDATE_MEMBER_ROLE');
INSERT INTO permissions (name) VALUES ('TEAM_INVITE_MEMBER');
INSERT INTO permissions (name) VALUES ('TEAM_READ_PROJECT');

INSERT INTO permissions (name) VALUES ('PROJECT_CREATE_STATUS');
INSERT INTO permissions (name) VALUES ('PROJECT_UPDATE_STATUS');
INSERT INTO permissions (name) VALUES ('PROJECT_DELETE_STATUS');

INSERT INTO permissions (name) VALUES ('PROJECT_CREATE_TASK');
INSERT INTO permissions (name) VALUES ('PROJECT_UPDATE_TASK');
INSERT INTO permissions (name) VALUES ('PROJECT_DELETE_TASK');

INSERT INTO permissions (name) VALUES ('TEAM_CREATE_PROJECT');
INSERT INTO permissions (name) VALUES ('PROJECT_UPDATE_PROJECT');
INSERT INTO permissions (name) VALUES ('PROJECT_DELETE_PROJECT');

INSERT INTO permissions (name) VALUES ('PROJECT_KICK_MEMBER');
INSERT INTO permissions (name) VALUES ('PROJECT_UPDATE_MEMBER_ROLE');
INSERT INTO permissions (name) VALUES ('PROJECT_INVITE_MEMBER');

-- TEAM_HEAD 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1); -- TEAM_DELETE_TEAM
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2); -- TEAM_UPDATE_TEAM
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 3); -- TEAM_KICK_MEMBER
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 4); -- TEAM_UPDATE_MEMBER_ROLE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 5); -- TEAM_INVITE_MEMBER
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 6); -- TEAM_READ_PROJECT
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 13); -- TEAM_CREATE_PROJECT

-- TEAM_LEADER 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 3); -- TEAM_KICK_MEMBER
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 6); -- TEAM_READ_PROJECT
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 13); -- TEAM_CREATE_PROJECT

-- TEAM_MATE 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 6); -- TEAM_READ_PROJECT

-- PROJECT_ADMIN 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 7); -- PROJECT_CREATE_STATUS
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 8); -- PROJECT_UPDATE_STATUS
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 9); -- PROJECT_DELETE_STATUS
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 10); -- PROJECT_CREATE_TASK
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 11); -- PROJECT_UPDATE_TASK
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 12); -- PROJECT_DELETE_TASK
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 14); -- PROJECT_UPDATE_PROJECT
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 15); -- PROJECT_DELETE_PROJECT
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 16); -- PROJECT_KICK_MEMBER
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 17); -- PROJECT_UPDATE_MEMBER_ROLE
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 18); -- PROJECT_INVITE_MEMBER

-- PROJECT_LEADER 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 7); -- PROJECT_CREATE_STATUS
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 8); -- PROJECT_UPDATE_STATUS
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 9); -- PROJECT_DELETE_STATUS
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 10); -- PROJECT_CREATE_TASK
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 11); -- PROJECT_UPDATE_TASK
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 12); -- PROJECT_DELETE_TASK

-- PROJECT_ASSIGNEE 역할에 대한 권한 설정
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 10); -- PROJECT_CREATE_TASK
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 11); -- PROJECT_UPDATE_TASK
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 12); -- PROJECT_DELETE_TASK
