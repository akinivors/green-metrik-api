package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import java.time.LocalDateTime;

public record ActivityLogResponse(
    Long id,
    LocalDateTime timestamp,
    String eventType,
    String description,
    String username
) {
    public static ActivityLogResponse fromEntity(ActivityLog log) {
        return new ActivityLogResponse(
            log.getId(),
            log.getTimestamp(),
            log.getEventType(),
            log.getDescription(),
            log.getUser().getUsername()
        );
    }
}
