package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.GreenMetricDTO;
import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricKeys;
import com.greenmetrik.greenmetrikapi.repository.CampusMetricsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GreenMetricService {

    private final CampusMetricsRepository campusMetricsRepository;

    public GreenMetricService(CampusMetricsRepository campusMetricsRepository) {
        this.campusMetricsRepository = campusMetricsRepository;
    }

    // ** NEW METHOD **
    public GreenMetricDTO.SettingAndInfrastructureStats calculateSettingAndInfrastructureStats() {
        // Fetch all necessary raw values
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

        // Return the DTO with calculated values
        return new GreenMetricDTO.SettingAndInfrastructureStats(
            openSpaceRatio,
            forestRatio,
            plantedRatio,
            absorptionRatio,
            campusPopulation,
            sustainabilityBudgetPercentage
        );
    }

    // ** NEW METHOD **
    public GreenMetricDTO.EnergyAndClimateChangeStats calculateEnergyAndClimateChangeStats() {
        // Fetch all necessary raw values
        double energyEfficientAppliances = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.ENERGY_EFFICIENT_APPLIANCES.key());
        double renewableEnergyProduction = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.TOTAL_RENEWABLE_ENERGY_PRODUCTION_KWH.key());
        double totalCarbonFootprint = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.TOTAL_CARBON_FOOTPRINT_TONS.key());

        // Fetch population for per-person calculation
        double totalStudents = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_STUDENTS.key());
        double academicStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ACADEMIC_STAFF.key());
        double adminStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ADMINISTRATIVE_STAFF.key());
        double campusPopulation = totalStudents + academicStaff + adminStaff;

        // Perform calculations
        // Assuming the raw value is a percentage already
        Double energyEfficientAppliancesUsage = energyEfficientAppliances;

        // This calculation might need total energy consumption later, for now, we return the raw production
        Double renewableEnergyProductionRatio = renewableEnergyProduction;

        Double carbonFootprintPerPerson = (campusPopulation > 0) ? (totalCarbonFootprint / campusPopulation) : 0.0;

        // Return the DTO with calculated values
        return new GreenMetricDTO.EnergyAndClimateChangeStats(
            energyEfficientAppliancesUsage,
            renewableEnergyProductionRatio,
            carbonFootprintPerPerson
        );
    }

    // ** NEW METHOD **
    public GreenMetricDTO.TransportationStats calculateTransportationStats() {
        // Fetch all necessary raw values
        double groundParkingArea = getMetricValueAsDouble(MetricKeys.Transportation.GROUND_PARKING_AREA_M2.key());
        double totalArea = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_CAMPUS_AREA_M2.key());
        int initiatives = getMetricValueAsInteger(MetricKeys.Transportation.INITIATIVES_TO_DECREASE_PRIVATE_VEHICLES.key());

        // Perform calculations
        Double groundParkingRatio = (totalArea > 0) ? (groundParkingArea / totalArea) * 100 : 0.0;

        // Return the DTO with calculated values
        return new GreenMetricDTO.TransportationStats(
            groundParkingRatio,
            initiatives
        );
    }

    // ** NEW METHOD **
    public GreenMetricDTO.EducationAndResearchStats calculateEducationAndResearchStats() {
        // Fetch all necessary raw values
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

        // Perform calculations
        Double coursesRatio = (totalCourses > 0) ? (sustainabilityCourses / totalCourses) * 100 : 0.0;
        Double fundingRatio = (totalResearchFunding > 0) ? (sustainabilityResearchFunding / totalResearchFunding) * 100 : 0.0;
        Double greenGraduatesRatio = (totalGraduates > 0) ? (greenJobsGraduates / totalGraduates) * 100 : 0.0;

        // Return the DTO with calculated values
        return new GreenMetricDTO.EducationAndResearchStats(
            coursesRatio,
            fundingRatio,
            sustainabilityPublications,
            sustainabilityEvents,
            studentActivities,
            culturalActivities,
            internationalPrograms,
            communityPrograms,
            sustainabilityStartups,
            greenGraduatesRatio
        );
    }

    // ** NEW HELPER METHOD **
    private double getMetricValueAsDouble(String metricKey) {
        return campusMetricsRepository.findByMetricKey(metricKey)
                .map(CampusMetrics::getMetricValue)
                .map(Double::parseDouble)
                .orElse(0.0);
    }

    private int getMetricValueAsInteger(String metricKey) {
        return campusMetricsRepository.findByMetricKey(metricKey)
                .map(CampusMetrics::getMetricValue)
                .map(Integer::parseInt)
                .orElse(0);
    }

    public GreenMetricDTO calculateAllGreenMetricStats() {
        // Call each of the individual calculation methods
        GreenMetricDTO.SettingAndInfrastructureStats siStats = calculateSettingAndInfrastructureStats();
        GreenMetricDTO.EnergyAndClimateChangeStats ecStats = calculateEnergyAndClimateChangeStats();
        GreenMetricDTO.TransportationStats trStats = calculateTransportationStats();
        GreenMetricDTO.EducationAndResearchStats edStats = calculateEducationAndResearchStats();

        // Assemble the final, complete DTO
        return new GreenMetricDTO(
            siStats,
            ecStats,
            trStats,
            edStats
        );
    }
}
