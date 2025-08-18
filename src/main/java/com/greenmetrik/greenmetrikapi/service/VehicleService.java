package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.VehicleEntryRequest;
import com.greenmetrik.greenmetrikapi.dto.VehicleEntryResponse;
import com.greenmetrik.greenmetrikapi.exception.ResourceNotFoundException;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.repository.VehicleEntryRepository;
import com.greenmetrik.greenmetrikapi.specifications.VehicleEntrySpecification;
import com.greenmetrik.greenmetrikapi.util.RepositoryHelper; // Import the new helper
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        VehicleEntry vehicleEntry = new VehicleEntry();
        vehicleEntry.setEntryDate(request.entryDate());
        vehicleEntry.setPublicTransportCount(request.publicTransportCount());
        vehicleEntry.setPrivateVehicleCount(request.privateVehicleCount());
        vehicleEntry.setMotorcycleCount(request.motorcycleCount());
        vehicleEntry.setZevCount(request.zevCount());
        vehicleEntry.setUser(user);

        vehicleEntryRepository.save(vehicleEntry);

        activityLogService.logActivity("CREATED", "VEHICLE_ENTRY", "Vehicle entry created for date: " + request.entryDate(), user);
    }

    public Page<VehicleEntryResponse> getAllVehicleEntries(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Pageable sortedPageable = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            Sort.by("entryDate").descending()
        );

        Specification<VehicleEntry> spec = Specification
                .where(VehicleEntrySpecification.hasEntryDateAfter(startDate))
                .and(VehicleEntrySpecification.hasEntryDateBefore(endDate));

        Page<VehicleEntry> entryPage = vehicleEntryRepository.findAll(spec, sortedPageable);
        return entryPage.map(VehicleEntryResponse::fromEntity);
    }

    public void deleteVehicleEntry(Long id, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        VehicleEntry entryToDelete = RepositoryHelper.findOrThrow(vehicleEntryRepository, id, "Vehicle entry");

        String description = "User '" + currentUsername + "' deleted a vehicle entry for date: " + entryToDelete.getEntryDate();
        activityLogService.logActivity("DELETED", "VEHICLE_ENTRY", description, user);

        vehicleEntryRepository.deleteById(id);
    }
}
