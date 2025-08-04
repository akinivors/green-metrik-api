package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public record VehicleEntryRequest(
    LocalDate entryDate,
    int publicTransportCount,
    int privateVehicleCount,
    int motorcycleCount,
    int zevCount
) {}
