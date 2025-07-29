package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.CampusMetricsRequest;
import com.greenmetrik.greenmetrikapi.dto.CampusMetricsResponse;
import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import com.greenmetrik.greenmetrikapi.repository.CampusMetricsRepository;
import com.greenmetrik.greenmetrikapi.specifications.CampusMetricsSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CampusMetricsService {

    private final CampusMetricsRepository campusMetricsRepository;

    public CampusMetricsService(CampusMetricsRepository campusMetricsRepository) {
        this.campusMetricsRepository = campusMetricsRepository;
    }

    public CampusMetricsResponse addMetric(CampusMetricsRequest request) {
        CampusMetrics metric = new CampusMetrics();
        metric.setMetricKey(request.metricKey());
        metric.setMetricValue(request.metricValue());
        metric.setCategory(MetricCategory.valueOf(request.category().toUpperCase()));
        metric.setMetricDate(request.metricDate());
        metric.setDescription(request.description());

        CampusMetrics savedMetric = campusMetricsRepository.save(metric);
        return CampusMetricsResponse.fromEntity(savedMetric);
    }

    public Page<CampusMetricsResponse> getAllMetrics(
            Pageable pageable,
            MetricCategory category,
            LocalDate startDate,
            LocalDate endDate) {

        Specification<CampusMetrics> spec = Specification
                .where(CampusMetricsSpecification.hasCategory(category))
                .and(CampusMetricsSpecification.hasMetricDateAfter(startDate))
                .and(CampusMetricsSpecification.hasMetricDateBefore(endDate));

        Page<CampusMetrics> metricsPage = campusMetricsRepository.findAll(spec, pageable);
        return metricsPage.map(CampusMetricsResponse::fromEntity);
    }
}
