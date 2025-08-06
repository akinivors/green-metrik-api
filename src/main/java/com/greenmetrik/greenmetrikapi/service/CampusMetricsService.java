package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.CampusMetricsRequest;
import com.greenmetrik.greenmetrikapi.dto.CampusMetricsResponse;
import com.greenmetrik.greenmetrikapi.exception.InvalidRequestException;
import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import com.greenmetrik.greenmetrikapi.model.MetricKeys;
import com.greenmetrik.greenmetrikapi.repository.CampusMetricsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CampusMetricsService {

    private final CampusMetricsRepository campusMetricsRepository;

    public CampusMetricsService(CampusMetricsRepository campusMetricsRepository) {
        this.campusMetricsRepository = campusMetricsRepository;
    }

    public CampusMetricsResponse addMetric(CampusMetricsRequest request) {
        // Step 1: Find the metric key to get its properties (like dataType)
        MetricKeys.MetricKey metricKeyInfo = MetricKeys.findByKey(request.metricKey());

        // Step 2: Validate the metric value based on its expected data type
        validateMetricValue(metricKeyInfo, request.metricValue());

        CampusMetrics metric = new CampusMetrics();
        metric.setMetricKey(metricKeyInfo.key());
        metric.setMetricValue(request.metricValue());
        metric.setCategory(MetricCategory.valueOf(request.category().toUpperCase()));
        metric.setMetricDate(request.metricDate());
        metric.setDescription(request.description());

        CampusMetrics savedMetric = campusMetricsRepository.save(metric);

        // Use our project's robust fromEntity method
        return CampusMetricsResponse.fromEntity(savedMetric);
    }

    public Page<CampusMetricsResponse> getAllMetrics(Pageable pageable, MetricCategory category, LocalDate startDate, LocalDate endDate) {
        String categoryName = (category != null) ? category.name() : null;

        Page<CampusMetrics> metricsPage = campusMetricsRepository.findLatestFiltered(pageable, categoryName, startDate, endDate);

        // Map the paged entities to DTOs using our robust fromEntity method
        return metricsPage.map(CampusMetricsResponse::fromEntity);
    }

    public void deleteMetric(Long id) {
        campusMetricsRepository.deleteById(id);
    }

    // This is the new, robust validation method from your coworker
    private void validateMetricValue(MetricKeys.MetricKey keyInfo, String value) {
        try {
            switch (keyInfo.dataType()) {
                case "NUMBER":
                    Double.parseDouble(value);
                    break;
                case "INTEGER":
                    Integer.parseInt(value);
                    break;
                // "STRING" requires no validation, so no case is needed.
            }
        } catch (NumberFormatException e) {
            throw new InvalidRequestException(
                    "Invalid value for metric '" + keyInfo.key() + "'. Expected a " + keyInfo.dataType().toLowerCase() + "."
            );
        }
    }
}