package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    // Custom query methods can be added here
}
