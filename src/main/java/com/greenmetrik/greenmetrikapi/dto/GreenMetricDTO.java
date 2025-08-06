package com.greenmetrik.greenmetrikapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GreenMetricDTO(
    SettingAndInfrastructureStats settingAndInfrastructure,
    EnergyAndClimateChangeStats energyAndClimateChange,
    WasteStats waste,
    WaterStats water,
    TransportationStats transportation,
    EducationAndResearchStats educationAndResearch
) {

    public record SettingAndInfrastructureStats(
        // Campus Profile Data
        String institutionType,
        String climateZone,
        String campusSetting,
        Integer numOfCampusSites,
        // Calculated Metrics
        Double openSpaceToTotalAreaRatio,
        Double forestVegetationToTotalAreaRatio,
        Double plantedVegetationToTotalAreaRatio,
        Double waterAbsorptionToTotalAreaRatio,
        Double campusPopulation,
        Double sustainabilityBudgetPercentage
    ) {}

    public record EnergyAndClimateChangeStats(
        Double energyEfficientAppliancesUsage,
        Double renewableEnergyProductionRatio,
        Double carbonFootprintPerPerson,
        Double smartBuildingRatio
    ) {}

    public record WasteStats(
        // Placeholder for future development
        Integer paperAndPlasticReductionPrograms
    ) {}

    public record WaterStats(
        Double waterConservationPercentage,
        Double waterEfficientAppliancePercentage
    ) {}

    public record TransportationStats(
        Double groundParkingToTotalAreaRatio,
        Integer initiativesToDecreasePrivateVehicles
    ) {}

    public record EducationAndResearchStats(
        Double sustainabilityCoursesRatio,
        Double sustainabilityResearchFundingRatio,
        Integer sustainabilityPublications,
        Integer sustainabilityEvents,
        Integer studentSustainabilityActivities,
        Integer culturalActivities,
        Integer internationalCollaborationPrograms,
        Integer communityServicePrograms,
        Integer sustainabilityStartups,
        Double greenJobsGraduatesRatio
    ) {}
}
