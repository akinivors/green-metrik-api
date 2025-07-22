package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionRequest;
import com.greenmetrik.greenmetrikapi.service.ConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
}
