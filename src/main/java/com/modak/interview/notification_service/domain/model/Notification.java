package com.modak.interview.notification_service.domain.model;

public record Notification(
        String type,
        String userId,
        String message
) {
}
