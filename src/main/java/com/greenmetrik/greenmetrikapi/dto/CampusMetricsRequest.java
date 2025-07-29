package com.greenmetrik.greenmetrikapi.dto;

public record CampusMetricsRequest(
    String metricKey,
    String metricValue,
    String category,
    Integer year,
    String description
) {}
