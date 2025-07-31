package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import java.time.format.DateTimeFormatter;

public class ActivityLogResponse {

    private final Long id;
    private final String timestamp;
    private final String eventType;
    private final String description;
    private final String username;

    public ActivityLogResponse(Long id, String timestamp, String eventType, String description, String username) {
        this.id = id;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.description = description;
        this.username = username;
    }

    public static ActivityLogResponse fromEntity(ActivityLog log) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return new ActivityLogResponse(
            log.getId(),
            log.getTimestamp().format(formatter),
            log.getEventType(),
            log.getDescription(),
            log.getUser().getUsername()
        );
    }

    // Standard Getters for JasperReports
    public Long getId() { return id; }
    public String getTimestamp() { return timestamp; }
    public String getEventType() { return eventType; }
    public String getDescription() { return description; }
    public String getUsername() { return username; }
}
