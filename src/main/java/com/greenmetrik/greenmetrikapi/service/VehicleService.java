package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.VehicleEntryRequest;
import com.greenmetrik.greenmetrikapi.dto.VehicleEntryResponse;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.repository.VehicleEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        vehicleEntry.setUser(user);

        vehicleEntryRepository.save(vehicleEntry);
    }

    public List<VehicleEntryResponse> getAllVehicleEntries() {
        return vehicleEntryRepository.findAll().stream()
                .map(VehicleEntryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
