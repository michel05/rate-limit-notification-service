package com.modak.interview.notification_service.domain.service;

import com.modak.interview.notification_service.domain.model.Notification;
import com.modak.interview.notification_service.domain.model.NotificationConfig;
import com.modak.interview.notification_service.domain.port.in.RateLimitValidationUseCase;
import com.modak.interview.notification_service.domain.port.out.NotificationConfigRepository;
import com.modak.interview.notification_service.domain.port.out.NotificationSentCacheRepository;
import org.springframework.stereotype.Service;

@Service
public class RateLimitValidationService implements RateLimitValidationUseCase {

    private final NotificationSentCacheRepository notificationSentCacheRepository;

    public RateLimitValidationService(NotificationConfigRepository notificationConfigRepository, NotificationSentCacheRepository notificationSentCacheRepository) {
        this.notificationSentCacheRepository = notificationSentCacheRepository;
    }

    @Override
    public Boolean validate(Notification notification, NotificationConfig notificationConfig) {
        var notificationSentCount = notificationSentCacheRepository.countRecentNotificationSent(
                notification.type(),
                notification.userId(),
                notificationConfig.getQuotaInSeconds()
        );

        return notificationSentCount < notificationConfig.getCountAllowed();
    }
}
