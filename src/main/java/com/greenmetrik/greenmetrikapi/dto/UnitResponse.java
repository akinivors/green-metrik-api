package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.Unit;

public record UnitResponse(Long id, String name) {
    public static UnitResponse fromEntity(Unit unit) {
        return new UnitResponse(unit.getId(), unit.getName());
    }
}
