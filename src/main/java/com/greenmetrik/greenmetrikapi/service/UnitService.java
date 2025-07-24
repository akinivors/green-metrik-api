package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.UnitResponse;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public List<UnitResponse> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(UnitResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
