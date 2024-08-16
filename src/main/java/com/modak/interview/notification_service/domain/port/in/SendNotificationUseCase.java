package com.modak.interview.notification_service.domain.port.in;

import com.modak.interview.notification_service.domain.model.Notification;

@FunctionalInterface
public interface SendNotificationUseCase {
    void send(Notification notification);
}
