package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.FoodData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDataRepository extends JpaRepository<FoodData, Long>, JpaSpecificationExecutor<FoodData> {
    // Custom query methods can be added here
}
