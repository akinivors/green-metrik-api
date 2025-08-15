package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import java.time.format.DateTimeFormatter;

public class ActivityLogResponse {

    private final Long id;
    private final String timestamp;
    private final String actionType;
    private final String dataType;
    private final String description;
    private final String username;

    public ActivityLogResponse(Long id, String timestamp, String actionType, String dataType, String description, String username) {
        this.id = id;
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.dataType = dataType;
        this.description = description;
        this.username = username;
    }

    public static ActivityLogResponse fromEntity(ActivityLog log) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return new ActivityLogResponse(
            log.getId(),
            log.getTimestamp().format(formatter),
            log.getActionType(),
            log.getDataType(),
            log.getDescription(),
            log.getUsername() // Use the safe, permanent username field
        );
    }

    // Standard Getters for JasperReports
    public Long getId() { return id; }
    public String getTimestamp() { return timestamp; }
    public String getActionType() { return actionType; }
    public String getDataType() { return dataType; }
    public String getDescription() { return description; }
    public String getUsername() { return username; }
}
