package com.modak.interview.notification_service.infrastructure.adapter.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_config")
public class NotificationConfigEntity {

    @Id
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "time_window_in_seconds", nullable = false)
    private Long timeWindowInSeconds;
    @Column(name = "max_notifications", nullable = false)
    private Long maxNotifications;

    public NotificationConfigEntity() {
    }

    public NotificationConfigEntity(String type, Long timeWindowInSeconds, Long countAllowed) {
        this.type = type;
        this.timeWindowInSeconds = timeWindowInSeconds;
        this.maxNotifications = countAllowed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimeWindowInSeconds() {
        return timeWindowInSeconds;
    }

    public void setTimeWindowInSeconds(Long timeWindowInSeconds) {
        this.timeWindowInSeconds = timeWindowInSeconds;
    }

    public Long getMaxNotifications() {
        return maxNotifications;
    }

    public void setMaxNotifications(Long maxNotifications) {
        this.maxNotifications = maxNotifications;
    }
}