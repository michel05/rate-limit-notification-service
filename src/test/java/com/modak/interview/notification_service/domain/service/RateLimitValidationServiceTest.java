package com.modak.interview.notification_service.domain.service;

import com.modak.interview.notification_service.domain.model.Notification;
import com.modak.interview.notification_service.domain.model.NotificationConfig;
import com.modak.interview.notification_service.domain.port.out.NotificationConfigRepository;
import com.modak.interview.notification_service.domain.port.out.NotificationSentCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RateLimitValidationServiceTest {

    @Mock
    private NotificationConfigRepository notificationConfigRepository;

    @Mock
    private NotificationSentCacheRepository notificationSentCacheRepository;

    private RateLimitValidationService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new RateLimitValidationService(notificationConfigRepository, notificationSentCacheRepository);
    }

    @Test
    @DisplayName("Given a notification and a notification config, " +
            "when the notification sent count is less than the count allowed, " +
            "then validation should return true.")
    public void test_whenNotificationSentCountIsLessThanCountAllowed_thenShouldReturnTrue() {
        Notification notification = new Notification("STATUS", "userId", "message");
        NotificationConfig notificationConfig = new NotificationConfig("STATUS", 60L, 5L);

        when(notificationSentCacheRepository.countRecentNotificationSent(
                notification.type(),
                notification.userId(),
                notificationConfig.getQuotaInSeconds())
        ).thenReturn(4L);

        boolean result = service.validate(notification, notificationConfig);

        assertTrue(result);
    }

    @Test
    @DisplayName("Given a notification and a notification config, " +
            "when the notification sent count is equal to the count allowed, " +
            "then validation should return false.")
    public void test_whenNotificationSentCountIsEqualToCountAllowed_thenShouldReturnFalse() {
        Notification notification = new Notification("STATUS", "userId", "message");
        NotificationConfig notificationConfig = new NotificationConfig("STATUS", 60L, 5L);

        when(notificationSentCacheRepository.countRecentNotificationSent(
                notification.type(),
                notification.userId(),
                notificationConfig.getQuotaInSeconds())
        ).thenReturn(5L);

        boolean result = service.validate(notification, notificationConfig);

        assertFalse(result);
    }
}