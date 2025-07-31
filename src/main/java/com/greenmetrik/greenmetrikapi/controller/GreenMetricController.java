package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.GreenMetricDTO;
import com.greenmetrik.greenmetrikapi.service.GreenMetricService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/green-metric")
public class GreenMetricController {

    private final GreenMetricService greenMetricService;

    public GreenMetricController(GreenMetricService greenMetricService) {
        this.greenMetricService = greenMetricService;
    }

    @GetMapping
    public GreenMetricDTO getGreenMetricStats() {
        return greenMetricService.calculateAllGreenMetricStats();
    }
}
