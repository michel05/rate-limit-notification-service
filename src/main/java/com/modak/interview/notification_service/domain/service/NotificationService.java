package com.modak.interview.notification_service.domain.service;

import com.modak.interview.notification_service.domain.exception.NotificationSendException;
import com.modak.interview.notification_service.domain.exception.RateLimitExceededException;
import com.modak.interview.notification_service.domain.model.Notification;
import com.modak.interview.notification_service.domain.port.in.RateLimitValidationUseCase;
import com.modak.interview.notification_service.domain.port.in.SendNotificationUseCase;
import com.modak.interview.notification_service.domain.port.out.NotificationConfigRepository;
import com.modak.interview.notification_service.domain.port.out.NotificationSenderGateway;
import com.modak.interview.notification_service.domain.port.out.NotificationSentCacheRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements SendNotificationUseCase {

    private final RateLimitValidationUseCase rateLimitValidation;
    private final NotificationSentCacheRepository notificationSentCacheRepository;
    private final NotificationConfigRepository notificationConfigRepository;
    private final NotificationSenderGateway notificationSenderGateway;


    public NotificationService(
            RateLimitValidationUseCase rateLimitValidation,
            NotificationSentCacheRepository notificationSentCacheRepository,
            NotificationConfigRepository notificationConfigRepository,
            NotificationSenderGateway notificationSenderGateway
    ) {
        this.rateLimitValidation = rateLimitValidation;
        this.notificationSentCacheRepository = notificationSentCacheRepository;
        this.notificationConfigRepository = notificationConfigRepository;
        this.notificationSenderGateway = notificationSenderGateway;
    }

    @Override
    public void send(Notification notification) {
        var notificationConfig = notificationConfigRepository.getNotificationConfig(notification.type());
        var validatedRateLimit = rateLimitValidation.validate(notification, notificationConfig);
        if (validatedRateLimit) {
            try {
                notificationSenderGateway.send(notification.type(), notification.userId(), notification.message());
                notificationSentCacheRepository.includeNotificationSent(notification.type(), notification.userId(), notificationConfig.getQuotaInSeconds());
            } catch (Exception e) {
                throw new NotificationSendException("Failed to send notification");
            }
        } else {
            throw new RateLimitExceededException("Notification already recently sent to user");
        }
    }
}
