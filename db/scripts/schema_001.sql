CREATE TABLE IF NOT EXISTS item
(
    id          SERIAL PRIMARY KEY,
    "desc"      TEXT,
    created     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    done        boolean   DEFAULT false
);