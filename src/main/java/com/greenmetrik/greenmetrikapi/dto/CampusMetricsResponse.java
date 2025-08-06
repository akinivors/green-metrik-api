package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import com.greenmetrik.greenmetrikapi.model.MetricKeys;
import java.time.LocalDate;

public record CampusMetricsResponse(
        Long id,
        String metricKey,
        String metricValue,
        MetricCategory category,
        LocalDate metricDate,
        String unit,
        String dataType, // <-- Include dataType
        String description
) {
    public static CampusMetricsResponse fromEntity(CampusMetrics entity) {
        // Look up the MetricKey to get its unit and data type
        MetricKeys.MetricKey metricKeyInfo = MetricKeys.findByKey(entity.getMetricKey());

        return new CampusMetricsResponse(
                entity.getId(),
                entity.getMetricKey(),
                entity.getMetricValue(),
                entity.getCategory(),
                entity.getMetricDate(),
                metricKeyInfo.unit(),
                metricKeyInfo.dataType(),
                entity.getDescription()
        );
    }
}