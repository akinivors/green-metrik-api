package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ElectricityConsumptionRequest;
import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.ElectricityConsumptionRepository;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionService {

    private final ElectricityConsumptionRepository electricityRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;

    public ConsumptionService(ElectricityConsumptionRepository electricityRepository, UserRepository userRepository, UnitRepository unitRepository) {
        this.electricityRepository = electricityRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
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
}
