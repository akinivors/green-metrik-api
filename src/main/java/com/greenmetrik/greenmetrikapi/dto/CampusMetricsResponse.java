package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;

public record CampusMetricsResponse(
    Long id,
    String metricKey,
    String metricValue,
    MetricCategory category,
    Integer year,
    String description
) {
    public static CampusMetricsResponse fromEntity(CampusMetrics entity) {
        return new CampusMetricsResponse(
            entity.getId(),
            entity.getMetricKey(),
            entity.getMetricValue(),
            entity.getCategory(),
            entity.getYear(),
            entity.getDescription()
        );
    }
}
