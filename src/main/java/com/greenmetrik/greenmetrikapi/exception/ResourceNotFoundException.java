package com.greenmetrik.greenmetrikapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceType;
    private final Object resourceId;

    public ResourceNotFoundException(String message, String resourceType, Object resourceId) {
        super(message);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    // Legacy constructor
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceType = "Unknown";
        this.resourceId = "Unknown";
    }

    public String getResourceType() {
        return resourceType;
    }

    public Object getResourceId() {
        return resourceId;
    }
}
