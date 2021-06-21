CREATE TABLE IF NOT EXISTS "user"
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(2000),
    email VARCHAR(254),
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