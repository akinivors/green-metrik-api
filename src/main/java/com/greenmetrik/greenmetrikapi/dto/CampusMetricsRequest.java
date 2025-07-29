package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public record CampusMetricsRequest(
    String metricKey,
    String metricValue,
    String category,
    LocalDate metricDate,
    String description
) {}
