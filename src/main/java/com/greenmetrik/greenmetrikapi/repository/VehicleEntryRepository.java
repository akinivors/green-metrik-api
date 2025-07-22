package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Long> {
    // Custom query methods can be added here
}
