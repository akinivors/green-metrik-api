package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricityConsumptionRepository extends JpaRepository<ElectricityConsumption, Long>, JpaSpecificationExecutor<ElectricityConsumption> {
}
