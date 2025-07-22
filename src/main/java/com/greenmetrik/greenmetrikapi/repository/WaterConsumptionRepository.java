package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.WaterConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterConsumptionRepository extends JpaRepository<WaterConsumption, Long> {
    // Custom query methods can be added here
}
