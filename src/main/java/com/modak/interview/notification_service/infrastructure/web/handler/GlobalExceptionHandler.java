package com.modak.interview.notification_service.infrastructure.web.handler;

import com.modak.interview.notification_service.domain.exception.DomainException;
import com.modak.interview.notification_service.domain.exception.NotificationTypeNotFoundException;
import com.modak.interview.notification_service.domain.exception.RateLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleException(DomainException ex) {
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        if (ex instanceof RateLimitExceededException) {
            return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
        } else if (ex instanceof NotificationTypeNotFoundException) {
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
