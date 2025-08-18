package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.GreenMetricDTO;
import com.greenmetrik.greenmetrikapi.exception.InvalidRequestException;
import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import com.greenmetrik.greenmetrikapi.model.MetricKeys;
import com.greenmetrik.greenmetrikapi.repository.CampusMetricsRepository;
import com.greenmetrik.greenmetrikapi.repository.ElectricityConsumptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GreenMetricService {

    private final CampusMetricsRepository campusMetricsRepository;
    private final ElectricityConsumptionRepository electricityRepository;

    public GreenMetricService(CampusMetricsRepository campusMetricsRepository, ElectricityConsumptionRepository electricityRepository) {
        this.campusMetricsRepository = campusMetricsRepository;
        this.electricityRepository = electricityRepository;
    }

    public GreenMetricDTO.SettingAndInfrastructureStats calculateSettingAndInfrastructureStats() {
        // Fetch new raw profile metrics
        String institutionType = getMetricValueAsString(MetricKeys.SettingAndInfrastructure.INSTITUTION_TYPE.key());
        String climateZone = getMetricValueAsString(MetricKeys.SettingAndInfrastructure.CLIMATE_ZONE.key());
        String campusSetting = getMetricValueAsString(MetricKeys.SettingAndInfrastructure.CAMPUS_SETTING.key());
        Integer numOfCampusSites = getMetricValueAsInteger(MetricKeys.SettingAndInfrastructure.NUM_OF_CAMPUS_SITES.key());

        // Fetch existing raw metrics for calculations
        double totalArea = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_CAMPUS_AREA_M2.key());
        double openSpaceArea = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.OPEN_SPACE_AREA_M2.key());
        double forestArea = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.FOREST_VEGETATION_AREA_M2.key());
        double plantedArea = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.PLANTED_VEGETATION_AREA_M2.key());
        double absorptionArea = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.WATER_ABSORPTION_AREA_M2.key());
        double totalStudents = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_STUDENTS.key());
        double academicStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ACADEMIC_STAFF.key());
        double adminStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ADMINISTRATIVE_STAFF.key());
        double totalBudget = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_UNIVERSITY_BUDGET.key());
        double sustainabilityBudget = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.SUSTAINABILITY_BUDGET.key());

        // Perform calculations
        Double openSpaceRatio = (totalArea > 0) ? (openSpaceArea / totalArea) * 100 : 0.0;
        Double forestRatio = (totalArea > 0) ? (forestArea / totalArea) * 100 : 0.0;
        Double plantedRatio = (totalArea > 0) ? (plantedArea / totalArea) * 100 : 0.0;
        Double absorptionRatio = (totalArea > 0) ? (absorptionArea / totalArea) * 100 : 0.0;
        Double campusPopulation = totalStudents + academicStaff + adminStaff;
        Double sustainabilityBudgetPercentage = (totalBudget > 0) ? (sustainabilityBudget / totalBudget) * 100 : 0.0;

        // Return the complete DTO with all fields
        return new GreenMetricDTO.SettingAndInfrastructureStats(
            institutionType, climateZone, campusSetting, numOfCampusSites,
            openSpaceRatio, forestRatio, plantedRatio, absorptionRatio, campusPopulation, sustainabilityBudgetPercentage
        );
    }

    public GreenMetricDTO.EnergyAndClimateChangeStats calculateEnergyAndClimateChangeStats() {
        // Fetch static raw values
        double energyEfficientAppliances = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.ENERGY_EFFICIENT_APPLIANCES.key());
        double renewableEnergyProduction = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.TOTAL_RENEWABLE_ENERGY_PRODUCTION_KWH.key());
        double totalCarbonFootprint = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.TOTAL_CARBON_FOOTPRINT_TONS.key());
        double smartBuildingArea = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.SMART_BUILDING_AREA_M2.key());
        double totalBuildingArea = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.TOTAL_BUILDING_AREA_M2.key());

        // Fetch dynamic consumption data
        double totalEnergyConsumption = getTotalElectricityConsumption();

        // Fetch population for per-person calculation
        double totalStudents = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_STUDENTS.key());
        double academicStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ACADEMIC_STAFF.key());
        double adminStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ADMINISTRATIVE_STAFF.key());
        double campusPopulation = totalStudents + academicStaff + adminStaff;

        // --- Perform All Calculations ---
        Double energyEfficientAppliancesUsage = energyEfficientAppliances;

        Double renewableEnergyProductionRatio = (totalEnergyConsumption > 0)
            ? (renewableEnergyProduction / totalEnergyConsumption) * 100
            : 0.0;

        Double carbonFootprintPerPerson = (campusPopulation > 0)
            ? (totalCarbonFootprint / campusPopulation)
            : 0.0;

        // NEW CALCULATION
        Double smartBuildingRatio = (totalBuildingArea > 0)
            ? (smartBuildingArea / totalBuildingArea) * 100
            : 0.0;

        // Return the complete DTO with all four fields
        return new GreenMetricDTO.EnergyAndClimateChangeStats(
            energyEfficientAppliancesUsage,
            renewableEnergyProductionRatio,
            carbonFootprintPerPerson,
            smartBuildingRatio
        );
    }

    public GreenMetricDTO.WasteStats calculateWasteStats() {
        Integer programs = getMetricValueAsInteger(MetricKeys.Waste.PAPER_PLASTIC_REDUCTION_PROGRAMS_COUNT.key());

        return new GreenMetricDTO.WasteStats(programs);
    }

    public GreenMetricDTO.WaterStats calculateWaterStats() {
        double conservationPercentage = getMetricValueAsDouble(MetricKeys.Water.WATER_CONSERVATION_PERCENTAGE.key());
        double appliancePercentage = getMetricValueAsDouble(MetricKeys.Water.WATER_EFFICIENT_APPLIANCE_PERCENTAGE.key());

        return new GreenMetricDTO.WaterStats(conservationPercentage, appliancePercentage);
    }

    public GreenMetricDTO.TransportationStats calculateTransportationStats() {
        double groundParkingArea = getMetricValueAsDouble(MetricKeys.Transportation.GROUND_PARKING_AREA_M2.key());
        double totalArea = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_CAMPUS_AREA_M2.key());
        int initiatives = getMetricValueAsInteger(MetricKeys.Transportation.INITIATIVES_TO_DECREASE_PRIVATE_VEHICLES.key());
        Double groundParkingRatio = (totalArea > 0) ? (groundParkingArea / totalArea) * 100 : 0.0;

        return new GreenMetricDTO.TransportationStats(groundParkingRatio, initiatives);
    }

    public GreenMetricDTO.EducationAndResearchStats calculateEducationAndResearchStats() {
        double sustainabilityCourses = getMetricValueAsDouble(MetricKeys.EducationAndResearch.SUSTAINABILITY_COURSES.key());
        double totalCourses = getMetricValueAsDouble(MetricKeys.EducationAndResearch.TOTAL_COURSES.key());
        double sustainabilityResearchFunding = getMetricValueAsDouble(MetricKeys.EducationAndResearch.SUSTAINABILITY_RESEARCH_FUNDING.key());
        double totalResearchFunding = getMetricValueAsDouble(MetricKeys.EducationAndResearch.TOTAL_RESEARCH_FUNDING.key());
        int sustainabilityPublications = getMetricValueAsInteger(MetricKeys.EducationAndResearch.SUSTAINABILITY_PUBLICATIONS.key());
        int sustainabilityEvents = getMetricValueAsInteger(MetricKeys.EducationAndResearch.SUSTAINABILITY_EVENTS.key());
        int studentActivities = getMetricValueAsInteger(MetricKeys.EducationAndResearch.STUDENT_SUSTAINABILITY_ACTIVITIES.key());
        int culturalActivities = getMetricValueAsInteger(MetricKeys.EducationAndResearch.CULTURAL_ACTIVITIES_COUNT.key());
        int internationalPrograms = getMetricValueAsInteger(MetricKeys.EducationAndResearch.INTERNATIONAL_COLLABORATION_PROGRAMS.key());
        int communityPrograms = getMetricValueAsInteger(MetricKeys.EducationAndResearch.COMMUNITY_SERVICE_PROGRAMS.key());
        int sustainabilityStartups = getMetricValueAsInteger(MetricKeys.EducationAndResearch.SUSTAINABILITY_STARTUPS.key());
        double greenJobsGraduates = getMetricValueAsDouble(MetricKeys.EducationAndResearch.GRADUATES_IN_GREEN_JOBS.key());
        double totalGraduates = getMetricValueAsDouble(MetricKeys.EducationAndResearch.TOTAL_GRADUATES.key());

        Double coursesRatio = (totalCourses > 0) ? (sustainabilityCourses / totalCourses) * 100 : 0.0;
        Double fundingRatio = (totalResearchFunding > 0) ? (sustainabilityResearchFunding / totalResearchFunding) * 100 : 0.0;
        Double greenGraduatesRatio = (totalGraduates > 0) ? (greenJobsGraduates / totalGraduates) * 100 : 0.0;

        return new GreenMetricDTO.EducationAndResearchStats(
            coursesRatio, fundingRatio, sustainabilityPublications, sustainabilityEvents, studentActivities,
            culturalActivities, internationalPrograms, communityPrograms, sustainabilityStartups, greenGraduatesRatio
        );
    }

    public GreenMetricDTO calculateAllGreenMetricStats() {
        GreenMetricDTO.SettingAndInfrastructureStats siStats = calculateSettingAndInfrastructureStats();
        GreenMetricDTO.EnergyAndClimateChangeStats ecStats = calculateEnergyAndClimateChangeStats();
        GreenMetricDTO.WasteStats wasteStats = calculateWasteStats();
        GreenMetricDTO.WaterStats waterStats = calculateWaterStats();
        GreenMetricDTO.TransportationStats trStats = calculateTransportationStats();
        GreenMetricDTO.EducationAndResearchStats edStats = calculateEducationAndResearchStats();

        return new GreenMetricDTO(siStats, ecStats, wasteStats, waterStats, trStats, edStats);
    }

    private double getTotalElectricityConsumption() {
        // Use the efficient repository method and handle the case where no data exists
        return electricityRepository.sumTotalConsumptionKwh().orElse(0.0);
    }

    // --- Using Coworker's More Robust Helper Methods ---
    private double getMetricValueAsDouble(String metricKey) {
        Optional<CampusMetrics> metricOpt = campusMetricsRepository.findByMetricKey(metricKey);
        if (metricOpt.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(metricOpt.get().getMetricValue());
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("The metric value for '" + metricKey + "' is not a valid number.");
        }
    }

    private int getMetricValueAsInteger(String metricKey) {
        Optional<CampusMetrics> metricOpt = campusMetricsRepository.findByMetricKey(metricKey);
        if (metricOpt.isEmpty()) return 0;
        try {
            return Integer.parseInt(metricOpt.get().getMetricValue());
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("The metric value for '" + metricKey + "' is not a valid integer.");
        }
    }

    private String getMetricValueAsString(String metricKey) {
        return campusMetricsRepository.findByMetricKey(metricKey)
                .map(CampusMetrics::getMetricValue)
                .orElse("");
    }
}
