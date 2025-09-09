CREATE TABLE blog.roles(
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) UNIQUE NOT NULL
);

CREATE TABLE blog.users(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    username VARCHAR(128) UNIQUE NOT NULL,
    email VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL,
    checked BOOLEAN DEFAULT FALSE,
    registred_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    photo bytea
);

CREATE TABLE blog.users_roles(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES blog.users(id),
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES blog.roles(id)
);

CREATE TABLE blog.posts(
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    text TEXT NOT NULL,
    created_by BIGINT NOT NULL,
    created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_user_id FOREIGN KEY (created_by) REFERENCES blog.users(id)
);

CREATE TABLE blog.comments(
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    author_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (author_id) REFERENCES blog.users(id),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES blog.posts(id)
);