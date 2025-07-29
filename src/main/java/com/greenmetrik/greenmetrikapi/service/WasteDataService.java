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

    public WasteDataService(WasteDataRepository wasteDataRepository, UserRepository userRepository) {
        this.wasteDataRepository = wasteDataRepository;
        this.userRepository = userRepository;
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
    }

    public Page<WasteDataResponse> getAllWasteData(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Specification<WasteData> spec = Specification
                .where(WasteDataSpecification.hasDataDateAfter(startDate))
                .and(WasteDataSpecification.hasDataDateBefore(endDate));

        Page<WasteData> dataPage = wasteDataRepository.findAll(spec, pageable);
        return dataPage.map(WasteDataResponse::fromEntity);
    }
}
