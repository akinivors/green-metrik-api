package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionRequest;
import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionResponse;
import com.greenmetrik.greenmetrikapi.dto.WaterConsumptionRequest;
import com.greenmetrik.greenmetrikapi.dto.WaterConsumptionResponse;
import com.greenmetrik.greenmetrikapi.service.ConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/consumption")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @PostMapping("/electricity")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'BINA_GOREVLISI')")
    public void addElectricityConsumption(@RequestBody ElectricityConsumptionRequest request, Principal principal) {
        consumptionService.addElectricityConsumption(request, principal.getName());
    }

    @PostMapping("/water")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'BINA_GOREVLISI')")
    public void addWaterConsumption(@RequestBody WaterConsumptionRequest request, Principal principal) {
        consumptionService.addWaterConsumption(request, principal.getName());
    }

    @GetMapping("/electricity")
    @PreAuthorize("isAuthenticated()")
    public List<ElectricityConsumptionResponse> getAllElectricityConsumption() {
        return consumptionService.getAllElectricityConsumption();
    }

    @GetMapping("/water")
    @PreAuthorize("isAuthenticated()")
    public List<WaterConsumptionResponse> getAllWaterConsumption() {
        return consumptionService.getAllWaterConsumption();
    }
}
