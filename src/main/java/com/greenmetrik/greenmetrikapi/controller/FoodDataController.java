package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.FoodDataRequest;
import com.greenmetrik.greenmetrikapi.dto.FoodDataResponse;
import com.greenmetrik.greenmetrikapi.service.FoodDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/entries")
public class FoodDataController {

    private final FoodDataService foodDataService;

    public FoodDataController(FoodDataService foodDataService) {
        this.foodDataService = foodDataService;
    }

    @PostMapping("/food")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'YEMEKHANE')")
    public void addFoodData(@RequestBody FoodDataRequest request, Principal principal) {
        foodDataService.addFoodData(request, principal.getName());
    }

    @GetMapping("/food")
    @PreAuthorize("isAuthenticated()")
    public Page<FoodDataResponse> getAllFoodData(
            Pageable pageable,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return foodDataService.getAllFoodData(pageable, startDate, endDate);
    }
}
