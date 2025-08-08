package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.validator.EndDateAfterStartDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@EndDateAfterStartDate(message = "Period end date cannot be before the start date.")
public record ElectricityConsumptionRequest(
    @NotNull(message = "Period start date cannot be null")
    LocalDate periodStartDate,

    @NotNull(message = "Period end date cannot be null")
    LocalDate periodEndDate,

    @NotNull(message = "Consumption kWh cannot be null")
    @PositiveOrZero(message = "Consumption kWh must be a positive number or zero")
    double consumptionKwh,

    @NotNull(message = "Unit ID cannot be null")
    Long unitId
) {}
