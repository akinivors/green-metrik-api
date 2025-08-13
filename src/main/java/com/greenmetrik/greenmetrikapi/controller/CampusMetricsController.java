package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.CampusMetricsRequest;
import com.greenmetrik.greenmetrikapi.dto.CampusMetricsResponse;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import com.greenmetrik.greenmetrikapi.service.CampusMetricsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/metrics")
public class CampusMetricsController {

    private final CampusMetricsService campusMetricsService;

    public CampusMetricsController(CampusMetricsService campusMetricsService) {
        this.campusMetricsService = campusMetricsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CampusMetricsResponse addMetric(@Valid @RequestBody CampusMetricsRequest request, Principal principal) {
        return campusMetricsService.addMetric(request, principal.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CampusMetricsResponse> getAllMetrics(
            Pageable pageable,
            @RequestParam(required = false) MetricCategory category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return campusMetricsService.getAllMetrics(pageable, category, startDate, endDate);
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CampusMetricsResponse> getMetricHistory(
            Pageable pageable,
            @RequestParam(required = false) MetricCategory category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return campusMetricsService.getMetricHistory(pageable, category, startDate, endDate);
    }

    @GetMapping("/{metricKey}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CampusMetricsResponse> getMetricHistoryByKey(
            @PathVariable String metricKey,
            Pageable pageable) {
        return campusMetricsService.getMetricHistoryByKey(metricKey, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMetric(@PathVariable Long id, Principal principal) {
        campusMetricsService.deleteMetric(id, principal.getName());
    }
}
