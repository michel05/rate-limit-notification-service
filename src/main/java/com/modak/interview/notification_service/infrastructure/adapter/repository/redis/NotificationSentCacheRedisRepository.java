package com.modak.interview.notification_service.infrastructure.adapter.repository.redis;

import com.modak.interview.notification_service.domain.port.out.NotificationSentCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * This class implements the NotificationSentCacheRepository interface and provides
 * a Redis-based implementation for storing and retrieving notification data.
 *
 * It uses a Redis Sorted Set (ZSET) to store the notifications sent by a user in a specific sorting (score).
 * Each notification is stored with a timestamp as its score, which allows for efficient
 * querying of recent notifications within a given time window.
 *
 * The key for each ZSET is generated using the notification type and user ID.
 */
@Repository
public class NotificationSentCacheRedisRepository implements NotificationSentCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public NotificationSentCacheRedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Counts the number of notifications sent by a user within a given time window.
     * This method will remove all the notifications that are older than the time window.
     *
     * @param type the type of the notifications
     * @param userId the ID of the user
     * @param timeWindowInSeconds the time window in seconds
     * @return the number of notifications sent by the user within the time window
     */
    @Override
    public Long countRecentNotificationSent(String type, String userId, Long timeWindowInSeconds) {
        String key = generateKey(type, userId);
        long now = Instant.now().toEpochMilli();
        long timeWindowMillis = TimeUnit.SECONDS.toMillis(timeWindowInSeconds);
        long threshold = now - timeWindowMillis;

        redisTemplate.opsForZSet().removeRangeByScore(key, 0, threshold);
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * Stores a notification sent by a user in Redis.
     * This will set up an expiry time for the notification.
     *
     * @param type the type of the notification
     * @param userId the ID of the user
     * @param timeToExpireInSeconds the time in seconds after which the notification should expire
     */
    @Override
    public void includeNotificationSent(String type, String userId, Long timeToExpireInSeconds) {
        String key = generateKey(type, userId);
        long now = Instant.now().toEpochMilli();

        redisTemplate.opsForZSet().add(key, String.valueOf(now), now);
        redisTemplate.expire(key, timeToExpireInSeconds, TimeUnit.SECONDS);
    }

    private String generateKey(String type, String userId) {
        return "notification:" + type.toLowerCase() + ":" + userId;
    }
}
