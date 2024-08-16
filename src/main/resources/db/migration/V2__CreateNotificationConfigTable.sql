CREATE TABLE IF NOT EXISTS notification_config (
    type VARCHAR(50),
    time_window_in_seconds BIGINT NOT NULL,
    max_notifications BIGINT NOT NULL,
    PRIMARY KEY (type)
);

INSERT INTO notification_config (type, time_window_in_seconds, max_notifications) VALUES ('STATUS', 120, 2);
INSERT INTO notification_config (type, time_window_in_seconds, max_notifications) VALUES ('NEWS', 86400, 1);
INSERT INTO notification_config (type, time_window_in_seconds, max_notifications) VALUES ('MARKETING', 3600, 3);
