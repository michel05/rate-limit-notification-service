-- Create notifications table partitioned by range on sent_at
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL,
    type VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, sent_at)
) PARTITION BY RANGE (sent_at);

-- Create initials partitions
CREATE TABLE IF NOT EXISTS notifications_2024_08 PARTITION OF notifications
FOR VALUES FROM ('2024-08-01 00:00:00') TO ('2024-09-01 00:00:00');

CREATE TABLE IF NOT EXISTS notifications_2024_09 PARTITION OF notifications
FOR VALUES FROM ('2024-09-01 00:00:00') TO ('2024-10-01 00:00:00');

CREATE TABLE IF NOT EXISTS notifications_2024_10 PARTITION OF notifications
FOR VALUES FROM ('2024-10-01 00:00:00') TO ('2024-11-01 00:00:00');


-- Add indexes to each partition
CREATE INDEX IF NOT EXISTS idx_user_id_type_sent_at_2024_08 ON notifications_2024_08(user_id, type, sent_at);
CREATE INDEX IF NOT EXISTS idx_sent_at_2024_08 ON notifications_2024_08(sent_at);

CREATE INDEX IF NOT EXISTS idx_user_id_type_sent_at_2024_09 ON notifications_2024_09(user_id, type, sent_at);
CREATE INDEX IF NOT EXISTS idx_sent_at_2024_09 ON notifications_2024_09(sent_at);

CREATE INDEX IF NOT EXISTS idx_user_id_type_sent_at_2024_10 ON notifications_2024_10(user_id, type, sent_at);
CREATE INDEX IF NOT EXISTS idx_sent_at_2024_10 ON notifications_2024_10(sent_at);

