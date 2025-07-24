package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.VehicleEntryRequest;
import com.greenmetrik.greenmetrikapi.dto.VehicleEntryResponse;
import com.greenmetrik.greenmetrikapi.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/entries")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'GUVENLIK')")
    public void addVehicleEntry(@RequestBody VehicleEntryRequest request, Principal principal) {
        vehicleService.addVehicleEntry(request, principal.getName());
    }

    @GetMapping("/vehicle")
    @PreAuthorize("hasAnyRole('ADMIN', 'GUVENLIK')")
    public Page<VehicleEntryResponse> getAllVehicleEntries(
            Pageable pageable,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return vehicleService.getAllVehicleEntries(pageable, startDate, endDate);
    }
}
