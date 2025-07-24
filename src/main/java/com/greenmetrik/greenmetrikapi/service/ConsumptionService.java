package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionRequest;
import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionResponse;
import com.greenmetrik.greenmetrikapi.dto.WaterConsumptionRequest;
import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.model.WaterConsumption;
import com.greenmetrik.greenmetrikapi.repository.ElectricityConsumptionRepository;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.repository.WaterConsumptionRepository;
import com.greenmetrik.greenmetrikapi.specifications.ElectricityConsumptionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ConsumptionService {

    private final ElectricityConsumptionRepository electricityRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final WaterConsumptionRepository waterRepository;

    public ConsumptionService(ElectricityConsumptionRepository electricityRepository, UserRepository userRepository, UnitRepository unitRepository, WaterConsumptionRepository waterRepository) {
        this.electricityRepository = electricityRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.waterRepository = waterRepository;
    }

    public void addElectricityConsumption(ElectricityConsumptionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        ElectricityConsumption consumption = new ElectricityConsumption();
        consumption.setPeriodStartDate(request.periodStartDate());
        consumption.setPeriodEndDate(request.periodEndDate());
        consumption.setConsumptionKwh(request.consumptionKwh());
        consumption.setUnit(unit);
        consumption.setUser(user);

        electricityRepository.save(consumption);
    }

    public void addWaterConsumption(WaterConsumptionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        WaterConsumption consumption = new WaterConsumption();
        consumption.setPeriodStartDate(request.periodStartDate());
        consumption.setPeriodEndDate(request.periodEndDate());
        consumption.setConsumptionTon(request.consumptionTon());
        consumption.setUnit(unit);
        consumption.setUser(user);

        waterRepository.save(consumption);
    }

    public Page<ElectricityConsumptionResponse> getAllElectricityConsumption(
            Pageable pageable, Long unitId, LocalDate startDate, LocalDate endDate) {

        Specification<ElectricityConsumption> spec = Specification
                .where(ElectricityConsumptionSpecification.hasUnitId(unitId))
                .and(ElectricityConsumptionSpecification.hasPeriodStartDateAfter(startDate))
                .and(ElectricityConsumptionSpecification.hasPeriodEndDateBefore(endDate));

        Page<ElectricityConsumption> consumptionPage = electricityRepository.findAll(spec, pageable);
        return consumptionPage.map(ElectricityConsumptionResponse::fromEntity);
    }
}
