package com.greenmetrik.greenmetrikapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record VehicleEntryRequest(
    @NotNull(message = "Entry date cannot be null")
    LocalDate entryDate,

    @NotNull(message = "Public transport count cannot be null")
    @PositiveOrZero(message = "Public transport count must be a positive number or zero")
    int publicTransportCount,

    @NotNull(message = "Private vehicle count cannot be null")
    @PositiveOrZero(message = "Private vehicle count must be a positive number or zero")
    int privateVehicleCount,

    @NotNull(message = "Motorcycle count cannot be null")
    @PositiveOrZero(message = "Motorcycle count must be a positive number or zero")
    int motorcycleCount,

    @NotNull(message = "ZEV count cannot be null")
    @PositiveOrZero(message = "ZEV count must be a positive number or zero")
    int zevCount
) {}
