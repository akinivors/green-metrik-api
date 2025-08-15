package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.CampusMetricsRequest;
import com.greenmetrik.greenmetrikapi.dto.CampusMetricsResponse;
import com.greenmetrik.greenmetrikapi.exception.InvalidRequestException;
import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import com.greenmetrik.greenmetrikapi.model.MetricKeys;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.CampusMetricsRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.specifications.CampusMetricsSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CampusMetricsService {

    private final CampusMetricsRepository campusMetricsRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public CampusMetricsService(CampusMetricsRepository campusMetricsRepository,
                               UserRepository userRepository,
                               ActivityLogService activityLogService) {
        this.campusMetricsRepository = campusMetricsRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    public CampusMetricsResponse addMetric(CampusMetricsRequest request, String username) {
        // Find the user for activity logging
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidRequestException("User not found: " + username));

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

        // Log the activity
        String message = String.format("Created metric '%s' with value '%s' for date %s",
                savedMetric.getMetricKey(),
                savedMetric.getMetricValue(),
                savedMetric.getMetricDate());
        activityLogService.logActivity("CREATED", "METRIC", message, user);

        // Use our project's robust fromEntity method
        return CampusMetricsResponse.fromEntity(savedMetric);
    }

    public Page<CampusMetricsResponse> getAllMetrics(Pageable pageable, MetricCategory category, LocalDate startDate, LocalDate endDate) {
        String categoryName = (category != null) ? category.name() : null;

        Page<CampusMetrics> metricsPage = campusMetricsRepository.findLatestFiltered(pageable, categoryName, startDate, endDate);

        // Map the paged entities to DTOs using our robust fromEntity method
        return metricsPage.map(CampusMetricsResponse::fromEntity);
    }

    public Page<CampusMetricsResponse> getMetricHistory(Pageable pageable, MetricCategory category, LocalDate startDate, LocalDate endDate) {
        // Build specification using CampusMetricsSpecification
        Specification<CampusMetrics> spec = Specification
                .where(CampusMetricsSpecification.hasCategory(category))
                .and(CampusMetricsSpecification.hasMetricDateAfter(startDate))
                .and(CampusMetricsSpecification.hasMetricDateBefore(endDate));

        // Create a new Pageable with descending sort by id
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        Page<CampusMetrics> metricsPage = campusMetricsRepository.findAll(spec, sortedPageable);

        // Map the paged entities to DTOs using our robust fromEntity method
        return metricsPage.map(CampusMetricsResponse::fromEntity);
    }

    public Page<CampusMetricsResponse> getMetricHistoryByKey(String metricKey, Pageable pageable) {
        Page<CampusMetrics> metricsPage = campusMetricsRepository.findByMetricKeyOrderByMetricDateDescIdDesc(metricKey, pageable);

        // Map the paged entities to DTOs using our robust fromEntity method
        return metricsPage.map(CampusMetricsResponse::fromEntity);
    }

    public void deleteMetric(Long id, String username) {
        // Find the user for activity logging
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidRequestException("User not found: " + username));

        // Find the metric before deleting for logging purposes
        CampusMetrics metric = campusMetricsRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Metric not found with id: " + id));

        // Log the activity before deletion
        String message = String.format("Deleted metric '%s' with value '%s' for date %s",
                metric.getMetricKey(),
                metric.getMetricValue(),
                metric.getMetricDate());
        activityLogService.logActivity("DELETED", "METRIC", message, user);

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