package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public record FoodDataRequest(
    LocalDate dataDate,
    double productionKg,
    double consumptionKg,
    double wasteOilLt
) {}
