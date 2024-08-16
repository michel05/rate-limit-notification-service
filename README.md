# Notification Service

The Notification Service is a Spring Boot application that provides an API for sending notifications. It uses a rate limiting mechanism to limit the number of notifications that a user can send within a certain time window.

## Rate Limiting Strategy

The rate limiting is implemented using Redis and its Sorted Set (ZSET) data structure. Each notification sent by a user is stored in a ZSET with a timestamp as its score. This allows for efficient querying of recent notifications within a given time window.

When a notification is sent, the application checks the number of notifications sent by the user within the time window. If the number of notifications exceeds a certain limit, the application will not send the notification and will return a "quota exceeded (Status 422)" error.

The key for each ZSET in Redis is generated using the notification type and user ID. This allows the application to apply rate limiting separately for each notification type and user.

## Notification Configuration

The application uses a `NotificationConfig` entity to store the configuration for each notification type. This includes the quota (i.e., the maximum number of notifications that can be sent within the time window) and the time window in seconds.

`NotificationConfigEntity`

| Field           | Type     | Description                                                                 |
|-----------------|----------|-----------------------------------------------------------------------------|
| `type`          | `String` | The type of the notification. It is the primary key of the table.           |
| `timeWindowInSeconds`| `Long`   | The quota for the notification type in seconds.                             |
| `maxNotifications`  | `Long`   | The maximum number of notifications that can be sent within the quota.      |

The `NotificationConfig` entities are stored in a PostgreSQL database and are cached in Redis for faster access. 

## External Resources
This application uses PostgreSQL and Redis, which can be easily set up using Docker Compose.

### Starting the Containers
```bash
docker-compose up -d
```


## API Endpoints

The application provides the following API endpoint:

- `POST /api/notifications/send`: Sends a notification. The request body should be a JSON object with the following properties:
    - `type`: The type of the notification.
    - `userId`: The ID of the user sending the notification.
    - `message`: The message of the notification.

## Running the Application

To run the application, you need to have Java 8 or higher and Maven installed on your machine. You also need to have a running instance of Redis and PostgreSQL.

You can then run the application using the following command:

```bash
mvn spring-boot:run
```

## API Documentation
The API documentation will be accessible at http://localhost:8080/swagger-ui/index.html
