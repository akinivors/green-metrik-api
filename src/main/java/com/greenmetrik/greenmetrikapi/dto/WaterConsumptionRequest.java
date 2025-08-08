package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.validator.EndDateAfterStartDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@EndDateAfterStartDate(message = "Period end date cannot be before the start date.")
public record WaterConsumptionRequest(
    @NotNull(message = "Period start date cannot be null")
    LocalDate periodStartDate,

    @NotNull(message = "Period end date cannot be null")
    LocalDate periodEndDate,

    @NotNull(message = "Consumption in tons cannot be null")
    @PositiveOrZero(message = "Consumption in tons must be a positive number or zero")
    double consumptionTon,

    @NotNull(message = "Recycled water usage cannot be null")
    @PositiveOrZero(message = "Recycled water usage must be a positive number or zero")
    double recycledWaterUsageLiters,

    @NotNull(message = "Treated water consumption cannot be null")
    @PositiveOrZero(message = "Treated water consumption must be a positive number or zero")
    double treatedWaterConsumptionLiters,

    @NotNull(message = "Unit ID cannot be null")
    Long unitId
) {}
