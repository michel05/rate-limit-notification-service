package com.modak.interview.notification_service.domain.service;

import com.modak.interview.notification_service.domain.exception.NotificationSendException;
import com.modak.interview.notification_service.domain.exception.RateLimitExceededException;
import com.modak.interview.notification_service.domain.model.Notification;
import com.modak.interview.notification_service.domain.model.NotificationConfig;
import com.modak.interview.notification_service.domain.port.in.RateLimitValidationUseCase;
import com.modak.interview.notification_service.domain.port.out.NotificationConfigRepository;
import com.modak.interview.notification_service.domain.port.out.NotificationSenderGateway;
import com.modak.interview.notification_service.domain.port.out.NotificationSentCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private RateLimitValidationUseCase rateLimitValidation;

    @Mock
    private NotificationSentCacheRepository notificationSentCacheRepository;

    @Mock
    private NotificationConfigRepository notificationConfigRepository;

    @Mock
    private NotificationSenderGateway notificationSenderGateway;

    private NotificationService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new NotificationService(rateLimitValidation, notificationSentCacheRepository, notificationConfigRepository, notificationSenderGateway);
    }

    @Test
    @DisplayName("Given a notification and a notification config, " +
            "when the rate limit is valid and the notification is sent, " +
            "then the notification should be included in the cache.")
    public void testSend_whenRateLimitIsValidatedAndNotificationIsSent_thenShouldIncludeNotificationInCache() {
        Notification notification = new Notification("STATUS", "userId", "message");
        NotificationConfig notificationConfig = new NotificationConfig("STATUS", 60L, 5L);

        when(rateLimitValidation.validate(notification, notificationConfig)).thenReturn(true);
        when(notificationConfigRepository.getNotificationConfig(notification.type())).thenReturn(notificationConfig);

        service.send(notification);

        verify(notificationSenderGateway, times(1))
                .send(notification.type(), notification.userId(), notification.message());
        verify(notificationSentCacheRepository, times(1))
                .includeNotificationSent(notification.type(), notification.userId(), notificationConfig.getQuotaInSeconds());
    }

    @Test
    @DisplayName("Given a notification and a notification config, " +
            "when the rate limit is not valid, " +
            "then a RateLimitExceededException should be thrown.")
    public void testSend_whenRateLimitIsNotValidated_thenShouldThrowRateLimitExceededException() {
        Notification notification = new Notification("STATUS", "userId", "message");
        NotificationConfig notificationConfig = new NotificationConfig("STATUS", 60L, 5L);

        when(rateLimitValidation.validate(notification, notificationConfig)).thenReturn(false);
        when(notificationConfigRepository.getNotificationConfig(notification.type())).thenReturn(notificationConfig);

        assertThrows(RateLimitExceededException.class, () -> service.send(notification));
    }

    @Test
    @DisplayName("Given a notification and a notification config, " +
            "when the notification sending fails, " +
            "then a NotificationSendException should be thrown.")
    public void testSend_whenNotificationSendingFails_thenShouldThrowNotificationSendException() {
        Notification notification = new Notification("STATUS", "userId", "message");
        NotificationConfig notificationConfig = new NotificationConfig("STATUS", 60L, 5L);

        when(rateLimitValidation.validate(notification, notificationConfig)).thenReturn(true);
        when(notificationConfigRepository.getNotificationConfig(notification.type())).thenReturn(notificationConfig);
        doThrow(new RuntimeException()).when(notificationSenderGateway).send(notification.type(), notification.userId(), notification.message());

        assertThrows(NotificationSendException.class, () -> service.send(notification));
    }
}