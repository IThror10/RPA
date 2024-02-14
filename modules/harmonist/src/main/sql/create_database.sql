CREATE TABLE IF NOT EXISTS users (
    id          serial  PRIMARY KEY,
    nickname    varchar NOT NULL UNIQUE,
    email       varchar NOT NULL UNIQUE,
    password    varchar NOT NULL,
    phone       varchar NULL UNIQUE,
    role        varchar NOT NULL DEFAULT ('ROLE_USER')
);

CREATE TABLE IF NOT EXISTS groups (
    id          serial PRIMARY KEY,
    leader      integer REFERENCES users (id),
    name        varchar NOT NULL UNIQUE,
    description text NOT NULL
);

CREATE TABLE IF NOT EXISTS members (
    user_id     integer REFERENCES users (id),
    group_id    integer REFERENCES groups (id),
    status      smallint NOT NULL,

    PRIMARY KEY (user_id, group_id)
);

CREATE TABLE IF NOT EXISTS robots (
    id          serial PRIMARY KEY,
    ver         varchar NOT NULL UNIQUE,
    ver_from    varchar NOT NULL,
    help        text NOT NULL
);

CREATE TABLE IF NOT EXISTS scripts (
    id          serial  PRIMARY KEY,
    code        text NOT NULL,
    name        varchar NOT NULL UNIQUE,
    description text NOT NULL,
    input_data  jsonb NOT NULL,

    version     varchar NOT NULL REFERENCES robots (ver),
    os          varchar NOT NULL,
    created     timestamp NOT NULL,
    creator     integer REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS authors (
    user_id     integer REFERENCES users (id),
    script_id   integer REFERENCES scripts (id),

    PRIMARY KEY (user_id, script_id)
);

CREATE TABLE IF NOT EXISTS executors (
    group_id    integer REFERENCES groups (id),
    script_id   integer REFERENCES scripts (id),

    PRIMARY KEY (group_id, script_id)
);
