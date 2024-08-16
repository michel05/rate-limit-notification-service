package com.modak.interview.notification_service.infrastructure.web;

import com.modak.interview.notification_service.domain.model.Notification;
import com.modak.interview.notification_service.domain.port.in.SendNotificationUseCase;
import com.modak.interview.notification_service.infrastructure.web.request.NotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SendNotificationUseCase sendNotificationUseCase;

    @Autowired
    public NotificationController(SendNotificationUseCase sendNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @Operation(summary = "Send notification")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Notification sent successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Notification reached quota limit for the user"
            )
    })
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(
            @RequestBody NotificationRequest request
    ) {
        sendNotificationUseCase.send(
                new Notification(
                        request.type(),
                        request.userId(),
                        request.message()
                )
        );
        return ResponseEntity.noContent().build();
    }
}
