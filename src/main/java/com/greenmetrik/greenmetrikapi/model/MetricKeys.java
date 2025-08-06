package com.greenmetrik.greenmetrikapi.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

public final class MetricKeys {

    private MetricKeys() {}

    // The improved record, including dataType
    public record MetricKey(String key, String unit, String dataType) {}

    public static final class SettingAndInfrastructure {
        private SettingAndInfrastructure() {}
        // All keys from both versions, now with dataType
        public static final MetricKey INSTITUTION_TYPE = new MetricKey("institution_type", "type", "STRING");
        public static final MetricKey CLIMATE_ZONE = new MetricKey("climate_zone", "zone", "STRING");
        public static final MetricKey CAMPUS_SETTING = new MetricKey("campus_setting", "setting", "STRING");
        public static final MetricKey NUM_OF_CAMPUS_SITES = new MetricKey("num_of_campus_sites", "count", "INTEGER");
        public static final MetricKey ICT_INFRASTRUCTURE_PROGRAM_STATUS = new MetricKey("ict_infrastructure_program_status", "status", "STRING");
        public static final MetricKey TOTAL_CAMPUS_AREA_M2 = new MetricKey("total_campus_area_m2", "m²", "NUMBER");
        public static final MetricKey OPEN_SPACE_AREA_M2 = new MetricKey("open_space_area_m2", "m²", "NUMBER");
        public static final MetricKey FOREST_VEGETATION_AREA_M2 = new MetricKey("forest_vegetation_area_m2", "m²", "NUMBER");
        public static final MetricKey PLANTED_VEGETATION_AREA_M2 = new MetricKey("planted_vegetation_area_m2", "m²", "NUMBER");
        public static final MetricKey WATER_ABSORPTION_AREA_M2 = new MetricKey("water_absorption_area_m2", "m²", "NUMBER");
        public static final MetricKey TOTAL_STUDENTS = new MetricKey("total_students", "people", "INTEGER");
        public static final MetricKey TOTAL_ACADEMIC_STAFF = new MetricKey("total_academic_staff", "people", "INTEGER");
        public static final MetricKey TOTAL_ADMINISTRATIVE_STAFF = new MetricKey("total_administrative_staff", "people", "INTEGER");
        public static final MetricKey TOTAL_UNIVERSITY_BUDGET = new MetricKey("total_university_budget", "USD", "NUMBER");
        public static final MetricKey SUSTAINABILITY_BUDGET = new MetricKey("sustainability_budget", "USD", "NUMBER");
        public static final MetricKey CONSERVATION_PROGRAM_IMPLEMENTATION_PERCENT = new MetricKey("conservation_program_implementation_percent", "%", "NUMBER");
    }

    public static final class EnergyAndClimateChange {
        private EnergyAndClimateChange() {}
        // All keys from both versions, now with dataType
        public static final MetricKey ENERGY_EFFICIENT_APPLIANCES = new MetricKey("energy_efficient_appliances", "%", "NUMBER");
        public static final MetricKey TOTAL_RENEWABLE_ENERGY_PRODUCTION_KWH = new MetricKey("total_renewable_energy_production_kwh", "kWh", "NUMBER");
        public static final MetricKey TOTAL_CARBON_FOOTPRINT_TONS = new MetricKey("total_carbon_footprint_tons", "tons CO₂e", "NUMBER");
        public static final MetricKey SMART_BUILDING_AREA_M2 = new MetricKey("smart_building_area_m2", "m²", "NUMBER");
        public static final MetricKey TOTAL_BUILDING_AREA_M2 = new MetricKey("total_building_area_m2", "m²", "NUMBER");
        public static final MetricKey RENEWABLE_ENERGY_SOURCE_COUNT = new MetricKey("renewable_energy_source_count", "count", "INTEGER");
        public static final MetricKey GREEN_BUILDING_ELEMENTS_COUNT = new MetricKey("green_building_elements_count", "count", "INTEGER");
        public static final MetricKey INNOVATIVE_ENERGY_PROGRAMS_COUNT = new MetricKey("innovative_energy_programs_count", "count", "INTEGER");
    }

    public static final class Waste {
        private Waste() {}
        // All keys from both versions, now with dataType
        public static final MetricKey PAPER_PLASTIC_REDUCTION_PROGRAMS_COUNT = new MetricKey("paper_plastic_reduction_programs_count", "count", "INTEGER");
        public static final MetricKey SEWAGE_TREATMENT_LEVEL = new MetricKey("sewage_treatment_level", "level", "STRING");
    }

    public static final class Water {
        private Water() {}
        // All keys from both versions, now with dataType
        public static final MetricKey WATER_CONSERVATION_PERCENTAGE = new MetricKey("water_conservation_percentage", "%", "NUMBER");
        public static final MetricKey WATER_EFFICIENT_APPLIANCE_PERCENTAGE = new MetricKey("water_efficient_appliance_percentage", "%", "NUMBER");
    }

    public static final class Transportation {
        private Transportation() {}
        // All keys from both versions, now with dataType
        public static final MetricKey GROUND_PARKING_AREA_M2 = new MetricKey("ground_parking_area_m2", "m²", "NUMBER");
        public static final MetricKey INITIATIVES_TO_DECREASE_PRIVATE_VEHICLES = new MetricKey("initiatives_to_decrease_private_vehicles", "count", "INTEGER");
    }

    public static final class EducationAndResearch {
        private EducationAndResearch() {}
        // All keys from both versions, now with dataType
        public static final MetricKey SUSTAINABILITY_COURSES = new MetricKey("sustainability_courses", "count", "INTEGER");
        public static final MetricKey TOTAL_COURSES = new MetricKey("total_courses", "count", "INTEGER");
        public static final MetricKey SUSTAINABILITY_RESEARCH_FUNDING = new MetricKey("sustainability_research_funding", "USD", "NUMBER");
        public static final MetricKey TOTAL_RESEARCH_FUNDING = new MetricKey("total_research_funding", "USD", "NUMBER");
        public static final MetricKey SUSTAINABILITY_PUBLICATIONS = new MetricKey("sustainability_publications", "count", "INTEGER");
        public static final MetricKey SUSTAINABILITY_EVENTS = new MetricKey("sustainability_events", "count", "INTEGER");
        public static final MetricKey STUDENT_SUSTAINABILITY_ACTIVITIES = new MetricKey("student_sustainability_activities", "count", "INTEGER");
        public static final MetricKey CULTURAL_ACTIVITIES_COUNT = new MetricKey("cultural_activities_count", "count", "INTEGER");
        public static final MetricKey INTERNATIONAL_COLLABORATION_PROGRAMS = new MetricKey("international_collaboration_programs", "count", "INTEGER");
        public static final MetricKey COMMUNITY_SERVICE_PROGRAMS = new MetricKey("community_service_programs", "count", "INTEGER");
        public static final MetricKey SUSTAINABILITY_STARTUPS = new MetricKey("sustainability_startups", "count", "INTEGER");
        public static final MetricKey GRADUATES_IN_GREEN_JOBS = new MetricKey("graduates_in_green_jobs", "count", "INTEGER");
        public static final MetricKey TOTAL_GRADUATES = new MetricKey("total_graduates", "count", "INTEGER");
    }

    public static MetricKey findByKey(String key) {
        return Stream.of(
            SettingAndInfrastructure.class.getFields(),
            EnergyAndClimateChange.class.getFields(),
            Waste.class.getFields(),
            Water.class.getFields(),
            Transportation.class.getFields(),
            EducationAndResearch.class.getFields()
        )
        .flatMap(Arrays::stream)
        .filter(field -> field.getType().equals(MetricKey.class))
        .map(MetricKeys::getMetricKeyFromField)
        .filter(metricKey -> metricKey.key().equals(key))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No metric found for key: " + key));
    }

    private static MetricKey getMetricKeyFromField(Field field) {
        try {
            return (MetricKey) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
