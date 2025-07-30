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

    public VehicleService(VehicleEntryRepository vehicleEntryRepository, UserRepository userRepository) {
        this.vehicleEntryRepository = vehicleEntryRepository;
        this.userRepository = userRepository;
    }

    public void addVehicleEntry(VehicleEntryRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VehicleEntry vehicleEntry = new VehicleEntry();
        vehicleEntry.setEntryDate(request.entryDate());
        vehicleEntry.setPublicTransportCount(request.publicTransportCount());
        vehicleEntry.setPrivateVehicleCount(request.privateVehicleCount());
        vehicleEntry.setZevCount(request.zevCount());
        vehicleEntry.setUser(user);

        vehicleEntryRepository.save(vehicleEntry);
    }

    public Page<VehicleEntryResponse> getAllVehicleEntries(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Specification<VehicleEntry> spec = Specification
                .where(VehicleEntrySpecification.hasEntryDateAfter(startDate))
                .and(VehicleEntrySpecification.hasEntryDateBefore(endDate));

        Page<VehicleEntry> entryPage = vehicleEntryRepository.findAll(spec, pageable);
        return entryPage.map(VehicleEntryResponse::fromEntity);
    }

    public void deleteVehicleEntry(Long id) {
        vehicleEntryRepository.deleteById(id);
    }
}
