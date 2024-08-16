package com.modak.interview.notification_service.domain.port.out;

import com.modak.interview.notification_service.domain.model.Notification;

public interface NotificationRepository {
    void saveNotification(Notification notification);
}
