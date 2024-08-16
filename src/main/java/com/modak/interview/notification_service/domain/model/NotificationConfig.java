package com.modak.interview.notification_service.domain.model;

import java.io.Serial;
import java.io.Serializable;

public class NotificationConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private String type;
    private Long quotaInSeconds;
    private Long countAllowed;

    public NotificationConfig() {
    }

    public NotificationConfig(
            String type,
            Long quotaInSeconds,
            Long countAllowed
    ) {
        this.type = type;
        this.quotaInSeconds = quotaInSeconds;
        this.countAllowed = countAllowed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getQuotaInSeconds() {
        return quotaInSeconds;
    }

    public void setQuotaInSeconds(Long quotaInSeconds) {
        this.quotaInSeconds = quotaInSeconds;
    }

    public Long getCountAllowed() {
        return countAllowed;
    }

    public void setCountAllowed(Long countAllowed) {
        this.countAllowed = countAllowed;
    }

    @Override
    public String toString() {
        return "NotificationConfig[" +
                "type=" + type + ", " +
                "quotaInSeconds=" + quotaInSeconds + ", " +
                "countAllowed=" + countAllowed + ']';
    }

}
