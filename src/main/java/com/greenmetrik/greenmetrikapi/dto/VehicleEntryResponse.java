package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import java.time.LocalDate;

public record VehicleEntryResponse(
    Long id, LocalDate entryDate, int publicTransportCount, int privateVehicleCount, String submittedByUsername
) {
    public static VehicleEntryResponse fromEntity(VehicleEntry entity) {
        return new VehicleEntryResponse(
            entity.getId(), entity.getEntryDate(), entity.getPublicTransportCount(),
            entity.getPrivateVehicleCount(), entity.getUser().getUsername()
        );
    }
}
