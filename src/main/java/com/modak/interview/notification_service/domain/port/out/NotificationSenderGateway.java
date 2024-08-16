package com.modak.interview.notification_service.domain.port.out;

public interface NotificationSenderGateway {

    void send(String type, String userId, String message);
}
