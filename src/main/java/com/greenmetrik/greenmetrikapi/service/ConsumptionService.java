package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionRequest;
import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionResponse;
import com.greenmetrik.greenmetrikapi.dto.WaterConsumptionRequest;
import com.greenmetrik.greenmetrikapi.dto.WaterConsumptionResponse;
import com.greenmetrik.greenmetrikapi.exception.ResourceNotFoundException;
import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.model.WaterConsumption;
import com.greenmetrik.greenmetrikapi.repository.ElectricityConsumptionRepository;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.repository.WaterConsumptionRepository;
import com.greenmetrik.greenmetrikapi.specifications.ElectricityConsumptionSpecification;
import com.greenmetrik.greenmetrikapi.specifications.WaterConsumptionSpecification;
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
    private final ActivityLogService activityLogService;

    public ConsumptionService(ElectricityConsumptionRepository electricityRepository, UserRepository userRepository, UnitRepository unitRepository, WaterConsumptionRepository waterRepository, ActivityLogService activityLogService) {
        this.electricityRepository = electricityRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.waterRepository = waterRepository;
        this.activityLogService = activityLogService;
    }

    public void addElectricityConsumption(ElectricityConsumptionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.unitId()));

        ElectricityConsumption consumption = new ElectricityConsumption();
        consumption.setPeriodStartDate(request.periodStartDate());
        consumption.setPeriodEndDate(request.periodEndDate());
        consumption.setConsumptionKwh(request.consumptionKwh());
        consumption.setUnit(unit);
        consumption.setUser(user);

        electricityRepository.save(consumption);

        // Log the electricity consumption creation activity
        activityLogService.logActivity("ELECTRICITY_LOG_CREATED", "Electricity consumption log created for unit: " + unit.getName(), user);
    }

    public void addWaterConsumption(WaterConsumptionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.unitId()));

        WaterConsumption consumption = new WaterConsumption();
        consumption.setPeriodStartDate(request.periodStartDate());
        consumption.setPeriodEndDate(request.periodEndDate());
        consumption.setConsumptionTon(request.consumptionTon());
        consumption.setRecycledWaterUsageLiters(request.recycledWaterUsageLiters());
        consumption.setTreatedWaterConsumptionLiters(request.treatedWaterConsumptionLiters());
        consumption.setUnit(unit);
        consumption.setUser(user);

        waterRepository.save(consumption);

        // Log the water consumption creation activity
        activityLogService.logActivity("WATER_LOG_CREATED", "Water consumption log created for unit: " + unit.getName(), user);
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

    public Page<WaterConsumptionResponse> getAllWaterConsumption(
            Pageable pageable, Long unitId, LocalDate startDate, LocalDate endDate) {

        Specification<WaterConsumption> spec = Specification
                .where(WaterConsumptionSpecification.hasUnitId(unitId))
                .and(WaterConsumptionSpecification.hasPeriodStartDateAfter(startDate))
                .and(WaterConsumptionSpecification.hasPeriodEndDateBefore(endDate));

        Page<WaterConsumption> consumptionPage = waterRepository.findAll(spec, pageable);
        return consumptionPage.map(WaterConsumptionResponse::fromEntity);
    }

    public void deleteElectricityConsumption(Long id, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        ElectricityConsumption entryToDelete = electricityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Electricity consumption entry not found with id: " + id));

        String description = "User '" + currentUsername + "' deleted an electricity log for unit '" + entryToDelete.getUnit().getName() + "' (Period: " + entryToDelete.getPeriodStartDate() + " to " + entryToDelete.getPeriodEndDate() + ")";
        activityLogService.logActivity("ELECTRICITY_LOG_DELETED", description, user);

        electricityRepository.deleteById(id);
    }

    public void deleteWaterConsumption(Long id, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        WaterConsumption entryToDelete = waterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Water consumption entry not found with id: " + id));

        String description = "User '" + currentUsername + "' deleted a water log for unit '" + entryToDelete.getUnit().getName() + "' (Period: " + entryToDelete.getPeriodStartDate() + " to " + entryToDelete.getPeriodEndDate() + ")";
        activityLogService.logActivity("WATER_LOG_DELETED", description, user);

        waterRepository.deleteById(id);
    }
}
