package com.modak.interview.notification_service.domain.exception;

public class RateLimitExceededException extends DomainException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
