package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.WasteDataRequest;
import com.greenmetrik.greenmetrikapi.dto.WasteDataResponse;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.model.WasteData;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.repository.WasteDataRepository;
import com.greenmetrik.greenmetrikapi.specifications.WasteDataSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WasteDataService {

    private final WasteDataRepository wasteDataRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public WasteDataService(WasteDataRepository wasteDataRepository, UserRepository userRepository, ActivityLogService activityLogService) {
        this.wasteDataRepository = wasteDataRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    public void addWasteData(WasteDataRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WasteData wasteData = new WasteData();
        wasteData.setDataDate(request.dataDate());
        wasteData.setOrganicProductionKg(request.organicProductionKg());
        wasteData.setOrganicConsumptionKg(request.organicConsumptionKg());
        wasteData.setOrganicTreatedKg(request.organicTreatedKg());
        wasteData.setInorganicProductionKg(request.inorganicProductionKg());
        wasteData.setInorganicConsumptionKg(request.inorganicConsumptionKg());
        wasteData.setInorganicRecycledKg(request.inorganicRecycledKg());
        wasteData.setToxicWasteKg(request.toxicWasteKg());
        wasteData.setTreatedToxicWasteKg(request.treatedToxicWasteKg());
        wasteData.setSewageDisposalLiters(request.sewageDisposalLiters());
        wasteData.setUser(user);

        wasteDataRepository.save(wasteData);

        // Log the waste data creation activity
        activityLogService.logActivity("WASTE_DATA_CREATED", "Waste data created for date: " + request.dataDate(), user);
    }

    public Page<WasteDataResponse> getAllWasteData(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Specification<WasteData> spec = Specification
                .where(WasteDataSpecification.hasDataDateAfter(startDate))
                .and(WasteDataSpecification.hasDataDateBefore(endDate));

        Page<WasteData> dataPage = wasteDataRepository.findAll(spec, pageable);
        return dataPage.map(WasteDataResponse::fromEntity);
    }

    public void deleteWasteData(Long id, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        WasteData entryToDelete = wasteDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Waste data entry not found"));

        String description = "User '" + currentUsername + "' deleted a waste data entry for date: " + entryToDelete.getDataDate();
        activityLogService.logActivity("WASTE_DATA_DELETED", description, user);

        wasteDataRepository.deleteById(id);
    }
}
