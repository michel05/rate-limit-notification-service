package com.modak.interview.notification_service.infrastructure.adapter.sender;

import com.modak.interview.notification_service.domain.port.out.NotificationSenderGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationSenderGatewayImpl implements NotificationSenderGateway {
    private static final Logger logger = LoggerFactory.getLogger(NotificationSenderGatewayImpl.class);

    @Override
    public void send(String type, String userId, String message) {
        logger.info("Sending notification of type: " + type + " to user: " + userId + " with message: " + message);
    }
}
