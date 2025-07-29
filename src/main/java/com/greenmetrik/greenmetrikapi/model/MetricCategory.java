package com.greenmetrik.greenmetrikapi.model;

public enum MetricCategory {
    SETTING_INFRASTRUCTURE("Setting and Infrastructure"),
    ENERGY_CLIMATE_CHANGE("Energy and Climate Change"),
    WASTE("Waste"),
    WATER("Water"),
    TRANSPORTATION("Transportation"),
    EDUCATION_RESEARCH("Education and Research");

    private final String displayName;

    MetricCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
