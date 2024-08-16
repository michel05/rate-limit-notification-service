package com.modak.interview.notification_service.domain.port.out;

public interface NotificationSentCacheRepository {
    Long countRecentNotificationSent(String type, String userId, Long timeWindowInSeconds);

    void includeNotificationSent(String type, String userId, Long timeToExpireInSeconds);
}
