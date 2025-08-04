package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.VehicleEntryRequest;
import com.greenmetrik.greenmetrikapi.dto.VehicleEntryResponse;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.repository.VehicleEntryRepository;
import com.greenmetrik.greenmetrikapi.specifications.VehicleEntrySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VehicleService {

    private final VehicleEntryRepository vehicleEntryRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public VehicleService(VehicleEntryRepository vehicleEntryRepository, UserRepository userRepository, ActivityLogService activityLogService) {
        this.vehicleEntryRepository = vehicleEntryRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    public void addVehicleEntry(VehicleEntryRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VehicleEntry vehicleEntry = new VehicleEntry();
        vehicleEntry.setEntryDate(request.entryDate());
        vehicleEntry.setPublicTransportCount(request.publicTransportCount());
        vehicleEntry.setPrivateVehicleCount(request.privateVehicleCount());
        vehicleEntry.setMotorcycleCount(request.motorcycleCount());
        vehicleEntry.setZevCount(request.zevCount());
        vehicleEntry.setUser(user);

        vehicleEntryRepository.save(vehicleEntry);

        // Log the vehicle entry creation activity
        activityLogService.logActivity("VEHICLE_ENTRY_CREATED", "Vehicle entry created for date: " + request.entryDate(), user);
    }

    public Page<VehicleEntryResponse> getAllVehicleEntries(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Specification<VehicleEntry> spec = Specification
                .where(VehicleEntrySpecification.hasEntryDateAfter(startDate))
                .and(VehicleEntrySpecification.hasEntryDateBefore(endDate));

        Page<VehicleEntry> entryPage = vehicleEntryRepository.findAll(spec, pageable);
        return entryPage.map(VehicleEntryResponse::fromEntity);
    }

    public void deleteVehicleEntry(Long id, String currentUsername) {
        // Find the user performing the action
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the entry to be deleted to get its details for logging
        VehicleEntry entryToDelete = vehicleEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle entry not found"));

        // Log the deletion event BEFORE deleting
        String description = "User '" + currentUsername + "' deleted a vehicle entry for date: " + entryToDelete.getEntryDate();
        activityLogService.logActivity("VEHICLE_ENTRY_DELETED", description, user);

        // Proceed with the deletion
        vehicleEntryRepository.deleteById(id);
    }
}
