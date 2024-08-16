package com.modak.interview.notification_service.domain.port.out;

import com.modak.interview.notification_service.domain.model.NotificationConfig;

public interface NotificationConfigRepository {
    NotificationConfig getNotificationConfig(String type);
}
