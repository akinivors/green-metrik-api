package com.greenmetrik.greenmetrikapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final List<Map<String, String>> errors;
    private final String errorCode; // NEW FIELD

    // Constructor for general errors (now includes errorCode)
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String errorCode) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = null;
        this.errorCode = errorCode;
    }

    // Constructor for validation errors (errorCode is null here)
    public ErrorResponse(LocalDateTime timestamp, int status, String error, List<Map<String, String>> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = null;
        this.errors = errors;
        this.errorCode = null;
    }

    // Legacy constructor for backward compatibility (will be phased out)
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
        this(timestamp, status, error, message, null);
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public List<Map<String, String>> getErrors() { return errors; }
    public String getErrorCode() { return errorCode; } // NEW GETTER
}
