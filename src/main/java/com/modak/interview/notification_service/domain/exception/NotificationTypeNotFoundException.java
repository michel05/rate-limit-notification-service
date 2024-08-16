package com.modak.interview.notification_service.domain.exception;

public class NotificationTypeNotFoundException extends DomainException {
    public NotificationTypeNotFoundException(String message) {
        super(message);
    }
}
