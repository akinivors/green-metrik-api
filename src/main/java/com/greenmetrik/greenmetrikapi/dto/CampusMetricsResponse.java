package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import com.greenmetrik.greenmetrikapi.model.MetricKeys; // Import MetricKeys
import java.time.LocalDate;

public record CampusMetricsResponse(
    Long id,
    String metricKey,
    String metricValue,
    MetricCategory category,
    LocalDate metricDate,
    String unit, // ** NEW FIELD **
    String description
) {
    public static CampusMetricsResponse fromEntity(CampusMetrics entity) {
        // Look up the MetricKey object to get its unit
        MetricKeys.MetricKey metricKeyInfo = MetricKeys.findByKey(entity.getMetricKey());

        return new CampusMetricsResponse(
            entity.getId(),
            entity.getMetricKey(),
            entity.getMetricValue(),
            entity.getCategory(),
            entity.getMetricDate(),
            metricKeyInfo.unit(), // ** SET THE UNIT **
            entity.getDescription()
        );
    }
}
