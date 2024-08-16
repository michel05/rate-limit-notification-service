package com.modak.interview.notification_service.domain.port.in;

import com.modak.interview.notification_service.domain.model.Notification;
import com.modak.interview.notification_service.domain.model.NotificationConfig;

public interface RateLimitValidationUseCase {
    Boolean validate(Notification notification, NotificationConfig notificationConfig);
}
