CREATE TABLE messages
(
    id             SERIAL PRIMARY KEY,
    ack_id         VARCHAR(255) NOT NULL UNIQUE,
    mobile         VARCHAR(255) NOT NULL,
    message        VARCHAR(160) NOT NULL,
    status         VARCHAR(255) NOT NULL,
    received_at    TIMESTAMP    NOT NULL,
    sent_at        TIMESTAMP,
    account_id     INTEGER      NOT NULL,
    telco_response TEXT,
    version        BIGINT
);


CREATE TABLE users
(
    account_id INTEGER PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP
);


CREATE TABLE sms_transactions
(
    id            SERIAL PRIMARY KEY,
    message_id    VARCHAR(255)  NOT NULL UNIQUE,
    account_id    INTEGER       NOT NULL,
    mobile        VARCHAR(255)  NOT NULL,
    message       VARCHAR(1000) NOT NULL,
    status        VARCHAR(255)  NOT NULL,
    created_at    TIMESTAMP     NOT NULL,
    processed_at  TIMESTAMP,
    delivered_at  TIMESTAMP,
    error_code    VARCHAR(255),
    error_message VARCHAR(255),
    priority      VARCHAR(255) DEFAULT 'NORMAL',
    message_type  VARCHAR(255) DEFAULT 'TEXT',
    credits_used  INTEGER,
    version       BIGINT
);


INSERT INTO users (account_id, username, password, is_active, created_at)
VALUES (1, 'user1', 'pass123', TRUE, CURRENT_TIMESTAMP),
       (2, 'user2', 'pass234', TRUE, CURRENT_TIMESTAMP),
       (3, 'user3', 'pass345', FALSE, CURRENT_TIMESTAMP),
       (4, 'user4', 'pass456', TRUE, CURRENT_TIMESTAMP),
       (5, 'user5', 'pass567', FALSE, CURRENT_TIMESTAMP);


select *
from messages;

