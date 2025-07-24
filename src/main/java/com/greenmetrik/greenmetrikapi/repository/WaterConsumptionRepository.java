package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.WaterConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterConsumptionRepository extends JpaRepository<WaterConsumption, Long>, JpaSpecificationExecutor<WaterConsumption> {
    // Custom query methods can be added here
}
