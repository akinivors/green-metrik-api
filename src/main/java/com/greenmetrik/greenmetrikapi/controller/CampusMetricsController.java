package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.CampusMetricsRequest;
import com.greenmetrik.greenmetrikapi.dto.CampusMetricsResponse;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import com.greenmetrik.greenmetrikapi.service.CampusMetricsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public CampusMetricsResponse addMetric(@RequestBody CampusMetricsRequest request) {
        return campusMetricsService.addMetric(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CampusMetricsResponse> getAllMetrics(
            Pageable pageable,
            @RequestParam(required = false) MetricCategory category,
            @RequestParam(required = false) Integer year) {
        return campusMetricsService.getAllMetrics(pageable, category, year);
    }
}
