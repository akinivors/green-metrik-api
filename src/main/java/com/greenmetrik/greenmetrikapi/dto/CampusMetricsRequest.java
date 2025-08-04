package com.greenmetrik.greenmetrikapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CampusMetricsRequest(
    @NotBlank(message = "Metric key cannot be blank")
    String metricKey,

    @NotBlank(message = "Metric value cannot be blank")
    String metricValue,

    @NotBlank(message = "Category cannot be blank")
    String category,

    @NotNull(message = "Metric date cannot be null")
    LocalDate metricDate,

    String description
) {}
