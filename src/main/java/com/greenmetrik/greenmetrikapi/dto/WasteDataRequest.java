package com.greenmetrik.greenmetrikapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record WasteDataRequest(
    @NotNull(message = "Data date cannot be null")
    LocalDate dataDate,

    @NotNull(message = "Organic production cannot be null")
    @PositiveOrZero(message = "Organic production must be a positive number or zero")
    double organicProductionKg,

    @NotNull(message = "Organic consumption cannot be null")
    @PositiveOrZero(message = "Organic consumption must be a positive number or zero")
    double organicConsumptionKg,

    @NotNull(message = "Organic treated cannot be null")
    @PositiveOrZero(message = "Organic treated must be a positive number or zero")
    double organicTreatedKg,

    @NotNull(message = "Inorganic production cannot be null")
    @PositiveOrZero(message = "Inorganic production must be a positive number or zero")
    double inorganicProductionKg,

    @NotNull(message = "Inorganic consumption cannot be null")
    @PositiveOrZero(message = "Inorganic consumption must be a positive number or zero")
    double inorganicConsumptionKg,

    @NotNull(message = "Inorganic recycled cannot be null")
    @PositiveOrZero(message = "Inorganic recycled must be a positive number or zero")
    double inorganicRecycledKg,

    @NotNull(message = "Toxic waste cannot be null")
    @PositiveOrZero(message = "Toxic waste must be a positive number or zero")
    double toxicWasteKg,

    @NotNull(message = "Treated toxic waste cannot be null")
    @PositiveOrZero(message = "Treated toxic waste must be a positive number or zero")
    double treatedToxicWasteKg,

    @NotNull(message = "Sewage disposal cannot be null")
    @PositiveOrZero(message = "Sewage disposal must be a positive number or zero")
    double sewageDisposalLiters
) {}
