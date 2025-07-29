package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import java.time.LocalDate;

public record CampusMetricsResponse(
    Long id,
    String metricKey,
    String metricValue,
    MetricCategory category,
    LocalDate metricDate,
    String description
) {
    public static CampusMetricsResponse fromEntity(CampusMetrics entity) {
        return new CampusMetricsResponse(
            entity.getId(),
            entity.getMetricKey(),
            entity.getMetricValue(),
            entity.getCategory(),
            entity.getMetricDate(),
            entity.getDescription()
        );
    }
}
