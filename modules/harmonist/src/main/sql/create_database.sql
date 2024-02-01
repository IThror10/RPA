CREATE TABLE IF NOT EXISTS users (
    id          serial  PRIMARY KEY,
    nickname    varchar NOT NULL UNIQUE,
    email       varchar NOT NULL UNIQUE,
    password    varchar NOT NULL,
    phone       varchar NULL UNIQUE,
    role        varchar NOT NULL DEFAULT ('user')
);

CREATE TABLE IF NOT EXISTS groups (
    id          serial PRIMARY KEY,
    leader      integer REFERENCES users (id),
    name        varchar NOT NULL UNIQUE,
    description text NOT NULL
);

CREATE TABLE IF NOT EXISTS participants (
    user_id     integer REFERENCES users (id),
    group_id    integer REFERENCES groups (id),
    status      integer NOT NULL,

    PRIMARY KEY (user_id, group_id)
);


CREATE TABLE IF NOT EXISTS scripts (
    id          serial  PRIMARY KEY,
    code        text NOT NULL,
    name        varchar NOT NULL,
    description text NOT NULL,
    input_data  jsonb NOT NULL,

    version     varchar NOT NULL,
    created     timestamp,
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

CREATE TABLE IF NOT EXISTS actions (
    user_id     integer REFERENCES users (id),
    script_id   integer REFERENCES scripts (id),
    time_begin  timestamp NOT NULL,
    time_exec   timestamp DEFAULT (NULL),
    input_data  varchar NULL,

    PRIMARY KEY (user_id, script_id, time_begin, time_exec)
);
