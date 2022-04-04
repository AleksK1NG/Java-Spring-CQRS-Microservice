CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP TABLE IF EXISTS orders CASCADE;


CREATE TABLE IF NOT EXISTS orders
(
    id               UUID                     DEFAULT uuid_generate_v4(),
    user_email       VARCHAR(250) NOT NULL CHECK ( user_email <> '' ),
    user_name        VARCHAR(250) NOT NULL CHECK ( user_name <> '' ),
    delivery_address VARCHAR(500) NOT NULL CHECK ( delivery_address <> '' ),
    status           VARCHAR(50)  NOT NULL CHECK ( status <> '' ),
    delivery_date    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (id, user_email)
);