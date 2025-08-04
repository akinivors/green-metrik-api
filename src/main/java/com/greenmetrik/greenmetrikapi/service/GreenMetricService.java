package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.GreenMetricDTO;
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

        Double openSpaceRatio = (totalArea > 0) ? (openSpaceArea / totalArea) * 100 : 0.0;
        Double forestRatio = (totalArea > 0) ? (forestArea / totalArea) * 100 : 0.0;
        Double plantedRatio = (totalArea > 0) ? (plantedArea / totalArea) * 100 : 0.0;
        Double absorptionRatio = (totalArea > 0) ? (absorptionArea / totalArea) * 100 : 0.0;
        Double campusPopulation = totalStudents + academicStaff + adminStaff;
        Double sustainabilityBudgetPercentage = (totalBudget > 0) ? (sustainabilityBudget / totalBudget) * 100 : 0.0;

        return new GreenMetricDTO.SettingAndInfrastructureStats(
            openSpaceRatio, forestRatio, plantedRatio, absorptionRatio, campusPopulation, sustainabilityBudgetPercentage
        );
    }

    public GreenMetricDTO.EnergyAndClimateChangeStats calculateEnergyAndClimateChangeStats() {
        double energyEfficientAppliances = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.ENERGY_EFFICIENT_APPLIANCES.key());
        double renewableEnergyProduction = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.TOTAL_RENEWABLE_ENERGY_PRODUCTION_KWH.key());
        double totalCarbonFootprint = getMetricValueAsDouble(MetricKeys.EnergyAndClimateChange.TOTAL_CARBON_FOOTPRINT_TONS.key());
        double totalEnergyConsumption = getTotalElectricityConsumption();
        double totalStudents = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_STUDENTS.key());
        double academicStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ACADEMIC_STAFF.key());
        double adminStaff = getMetricValueAsDouble(MetricKeys.SettingAndInfrastructure.TOTAL_ADMINISTRATIVE_STAFF.key());
        double campusPopulation = totalStudents + academicStaff + adminStaff;

        Double renewableEnergyProductionRatio = (totalEnergyConsumption > 0) ? (renewableEnergyProduction / totalEnergyConsumption) * 100 : 0.0;
        Double carbonFootprintPerPerson = (campusPopulation > 0) ? (totalCarbonFootprint / campusPopulation) : 0.0;

        return new GreenMetricDTO.EnergyAndClimateChangeStats(
            energyEfficientAppliances, renewableEnergyProductionRatio, carbonFootprintPerPerson
        );
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
        GreenMetricDTO.WaterStats waterStats = calculateWaterStats();
        GreenMetricDTO.TransportationStats trStats = calculateTransportationStats();
        GreenMetricDTO.EducationAndResearchStats edStats = calculateEducationAndResearchStats();

        return new GreenMetricDTO(siStats, ecStats, waterStats, trStats, edStats);
    }

    private double getTotalElectricityConsumption() {
        return electricityRepository.findAll().stream()
                .mapToDouble(ElectricityConsumption::getConsumptionKwh)
                .sum();
    }

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
}
