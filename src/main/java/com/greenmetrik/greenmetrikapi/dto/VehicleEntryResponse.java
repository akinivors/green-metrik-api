package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import java.time.LocalDate;

public record VehicleEntryResponse(
    Long id,
    LocalDate entryDate,
    int publicTransportCount,
    int privateVehicleCount,
    int motorcycleCount,
    int zevCount,
    String submittedByUsername
) {
    public static VehicleEntryResponse fromEntity(VehicleEntry entity) {
        return new VehicleEntryResponse(
            entity.getId(),
            entity.getEntryDate(),
            entity.getPublicTransportCount(),
            entity.getPrivateVehicleCount(),
            entity.getMotorcycleCount(),
            entity.getZevCount(),
            entity.getUser().getUsername()
        );
    }
}
