CREATE TABLE IF NOT EXISTS "user"
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(2000),
    email VARCHAR(254) UNIQUE,
    password TEXT
);

CREATE TABLE IF NOT EXISTS item
(
    id      SERIAL PRIMARY KEY,
    "desc"  TEXT,
    created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    done    BOOLEAN                  DEFAULT false,
    user_id INT NOT NULL REFERENCES "user"(id)
);

CREATE TABLE IF NOT EXISTS category
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(64)
);

create table IF NOT EXISTS item_category
(
    task_id integer not null
        constraint fkrk08yseujdcw50xspea10dcv8
            references item,
    categories_id integer not null
        constraint fkrxqiuae22nayh8eslm3wlj7j0
            references category
);


