package com.modak.interview.notification_service.infrastructure.adapter.repository;

import com.modak.interview.notification_service.infrastructure.adapter.repository.entity.NotificationConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationConfigRepository extends JpaRepository<NotificationConfigEntity, String> {
}
