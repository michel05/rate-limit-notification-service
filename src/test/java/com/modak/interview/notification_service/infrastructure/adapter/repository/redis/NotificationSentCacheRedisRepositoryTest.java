package com.modak.interview.notification_service.infrastructure.adapter.repository.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NotificationSentCacheRedisRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    private NotificationSentCacheRedisRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        repository = new NotificationSentCacheRedisRepository(redisTemplate);
    }

    @Test
    @DisplayName("Given a notification type, user ID, and time window, " +
            "when counting recent notification sent is called, " +
            "then older notification is removed and recent notification sent is counted.")
    public void test_whenCountingRecentNotificationSentIsCalled_thenShouldReturnTheCount() {
        String type = "type";
        String userId = "userId";
        Long timeWindowInSeconds = 60L;

        var count = repository.countRecentNotificationSent(type, userId, timeWindowInSeconds);

        assertEquals(0, count);
        verify(zSetOperations, times(1)).removeRangeByScore(anyString(), anyDouble(), anyDouble());
        verify(zSetOperations, times(1)).size(anyString());
    }

    @Test
    @DisplayName("Given a notification type, user ID, and time to expire, " +
            "when inclusion on cache is called, " +
            "then notification sent with expiration is included on cache.")
    public void test_whenIncludeNotificationIsCalled_thenTheItemShouldBeIncludedWithExpirationTime() {
        String type = "type";
        String userId = "userId";
        Long timeToExpireInSeconds = 60L;

        repository.includeNotificationSent(type, userId, timeToExpireInSeconds);

        verify(zSetOperations, times(1)).add(anyString(), anyString(), anyDouble());
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any());
    }
}