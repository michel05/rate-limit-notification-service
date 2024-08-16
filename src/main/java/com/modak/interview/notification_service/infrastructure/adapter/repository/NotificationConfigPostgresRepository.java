package com.modak.interview.notification_service.infrastructure.adapter.repository;

import com.modak.interview.notification_service.domain.model.NotificationConfig;
import com.modak.interview.notification_service.domain.port.out.NotificationConfigRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationConfigPostgresRepository implements NotificationConfigRepository {

    private final JpaNotificationConfigRepository repository;

    public NotificationConfigPostgresRepository(JpaNotificationConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "notificationConfig", key = "#type")
    public NotificationConfig getNotificationConfig(String type) {
        return repository.findById(type)
                .map(entity -> new NotificationConfig(entity.getType(), entity.getTimeWindowInSeconds(), entity.getMaxNotifications()))
                .orElseThrow(() -> new RuntimeException("Notification config not found"));
    }
}
