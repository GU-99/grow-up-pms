-- 역할/권한
CREATE TABLE IF NOT EXISTS roles (
    id          BIGINT                  AUTO_INCREMENT PRIMARY KEY,
    type        ENUM('TEAM', 'PROJECT') NOT NULL,
    name        VARCHAR(20)             NOT NULL,
    UNIQUE KEY  uk_roles_type_name (type, name)
);

CREATE TABLE IF NOT EXISTS permissions (
    id      BIGINT          AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id         BIGINT  NOT NULL,
    permission_id   BIGINT  NOT NULL,
    CONSTRAINT  fk_role_permission_role       FOREIGN KEY (role_id)       REFERENCES roles (id),
    CONSTRAINT  fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permissions (id),
    PRIMARY KEY (role_id, permission_id)
);

-- 사용자
CREATE TABLE IF NOT EXISTS users (
    id                      BIGINT                              AUTO_INCREMENT PRIMARY KEY,
    username                VARCHAR(32)                         NOT NULL UNIQUE,
    password                VARCHAR(128),
    email                   VARCHAR(128)                        NOT NULL,
    provider                ENUM('LOCAL', 'KAKAO', 'GOOGLE')    NOT NULL,
    nickname                VARCHAR(20)                         NOT NULL,
    bio                     TEXT,
    image                   VARCHAR(255),
    image_name              VARCHAR(255),
    password_change_date    DATETIME                            NOT NULL,
    password_failure_count  INT                                 NOT NULL DEFAULT 0,
    created_at              DATETIME,
    updated_at              DATETIME,
    is_deleted              BOOLEAN                             NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS user_links (
    id          BIGINT      AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT      NOT NULL,
    link                    VARCHAR(255) NOT NULL,
    created_at  DATETIME,
    updated_at  DATETIME,
    is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_user_link FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 팀
CREATE TABLE IF NOT EXISTS teams (
    id          BIGINT AUTO_INCREMENT   PRIMARY KEY,
    creator_id  BIGINT                  NOT NULL,
    name        VARCHAR(10)             NOT NULL,
    content     VARCHAR(200),
    created_at  DATETIME,
    updated_at  DATETIME,
    is_deleted  BOOLEAN                 NOT NULL DEFAULT FALSE,
    CONSTRAINT  fk_team_creator FOREIGN KEY (creator_id) REFERENCES users(id),
    UNIQUE KEY  uk_team_creator_name (creator_id, name)
);

CREATE TABLE IF NOT EXISTS team_users (
    team_id             BIGINT      NOT NULL,
    user_id             BIGINT      NOT NULL,
    role_id             BIGINT      NOT NULL,
    is_pending_approval BOOLEAN     NOT NULL,
    is_deleted          BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at          DATETIME,
    updated_at          DATETIME,
    PRIMARY KEY (team_id, user_id),
    CONSTRAINT fk_team_user_team FOREIGN KEY (team_id) REFERENCES teams (id),
    CONSTRAINT fk_team_user_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_team_user_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- 프로젝트
CREATE TABLE IF NOT EXISTS projects (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    team_id     BIGINT          NOT NULL,
    name        VARCHAR(128)    NOT NULL,
    content     VARCHAR(200),
    start_date  DATE,
    end_date    DATE,
    created_at  DATETIME,
    updated_at  DATETIME,
    is_deleted  BOOLEAN         NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_project_team FOREIGN KEY (team_id) REFERENCES teams(id)
);

-- 프로젝트 유저
CREATE TABLE project_users (
    project_id          BIGINT  NOT NULL,
    user_id             BIGINT  NOT NULL,
    role_id             BIGINT  NOT NULL,
    is_pending_approval BOOLEAN NOT NULL,
    is_deleted          BOOLEAN   DEFAULT FALSE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (project_id, user_id),

    CONSTRAINT fk_project_user_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT fk_project_user_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_project_user_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- 프로젝트 상태
CREATE TABLE IF NOT EXISTS project_status (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    project_id  BIGINT          NOT NULL,
    name        VARCHAR(32)     NOT NULL,
    color_code  CHAR(6)         NOT NULL,
    sort_order  SMALLINT        NOT NULL,
    is_deleted  BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at  DATETIME,
    updated_at  DATETIME,
    CONSTRAINT fk_status_project FOREIGN KEY (project_id) REFERENCES projects (id)
);

-- 프로젝트 일정
CREATE TABLE IF NOT EXISTS status_tasks (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    project_status_id   BIGINT          NOT NULL,
    user_id             BIGINT,
    name                VARCHAR(128)    NOT NULL,
    content             LONGTEXT,
    sort_order          SMALLINT        NOT NULL,
    start_date          DATE,
    end_date            DATE,
    is_deleted          BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at          DATETIME,
    updated_at          DATETIME,
    CONSTRAINT fk_task_status FOREIGN KEY (project_status_id)  REFERENCES project_status (id),
    CONSTRAINT fk_task_user   FOREIGN KEY (user_id)            REFERENCES users (id)
);

-- 프로젝트 일정 유저
CREATE TABLE IF NOT EXISTS task_users (
    user_id    BIGINT NOT NULL,
    task_id    BIGINT NOT NULL,
    is_deleted BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, task_id),

    CONSTRAINT fk_task_users_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_task_users_task FOREIGN KEY (task_id) REFERENCES status_tasks (id)
);
