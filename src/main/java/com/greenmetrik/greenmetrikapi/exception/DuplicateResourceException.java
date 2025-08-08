package com.greenmetrik.greenmetrikapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    private final String resourceType;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String message, String resourceType, String field, Object value) {
        super(message);
        this.resourceType = resourceType;
        this.field = field;
        this.value = value;
    }

    // Legacy constructor
    public DuplicateResourceException(String message) {
        super(message);
        this.resourceType = "Unknown";
        this.field = "Unknown";
        this.value = "Unknown";
    }

    public String getResourceType() { return resourceType; }
    public String getField() { return field; }
    public Object getValue() { return value; }
}
