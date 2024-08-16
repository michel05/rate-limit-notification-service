package com.modak.interview.notification_service.infrastructure.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationRequest(
        @JsonProperty("type")
        String type,

        @JsonProperty("user_id")
        String userId,

        @JsonProperty("message")
        String message
) {}