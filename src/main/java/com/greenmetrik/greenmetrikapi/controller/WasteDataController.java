package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.WasteDataRequest;
import com.greenmetrik.greenmetrikapi.dto.WasteDataResponse;
import com.greenmetrik.greenmetrikapi.service.WasteDataService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/entries")
public class WasteDataController {

    private final WasteDataService wasteDataService;

    public WasteDataController(WasteDataService wasteDataService) {
        this.wasteDataService = wasteDataService;
    }

    @PostMapping("/waste")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'YEMEKHANE')")
    public void addWasteData(@Valid @RequestBody WasteDataRequest request, Principal principal) {
        wasteDataService.addWasteData(request, principal.getName());
    }

    @GetMapping("/waste")
    @PreAuthorize("hasAnyRole('ADMIN', 'YEMEKHANE')")
    public Page<WasteDataResponse> getAllWasteData(
            Pageable pageable,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return wasteDataService.getAllWasteData(pageable, startDate, endDate);
    }

    @DeleteMapping("/waste/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'YEMEKHANE')")
    public void deleteWasteData(@PathVariable Long id, Principal principal) {
        wasteDataService.deleteWasteData(id, principal.getName());
    }
}
