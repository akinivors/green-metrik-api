package com.greenmetrik.greenmetrikapi.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

public final class MetricKeys {

    // Private constructor to prevent instantiation
    private MetricKeys() {}

    // NEW: A record to hold both key and unit
    public record MetricKey(String key, String unit) {}

    public static final class SettingAndInfrastructure {
        private SettingAndInfrastructure() {}
        public static final MetricKey TOTAL_CAMPUS_AREA_M2 = new MetricKey("total_campus_area_m2", "m²");
        public static final MetricKey OPEN_SPACE_AREA_M2 = new MetricKey("open_space_area_m2", "m²");
        public static final MetricKey FOREST_VEGETATION_AREA_M2 = new MetricKey("forest_vegetation_area_m2", "m²");
        public static final MetricKey PLANTED_VEGETATION_AREA_M2 = new MetricKey("planted_vegetation_area_m2", "m²");
        public static final MetricKey WATER_ABSORPTION_AREA_M2 = new MetricKey("water_absorption_area_m2", "m²");
        public static final MetricKey TOTAL_STUDENTS = new MetricKey("total_students", "people");
        public static final MetricKey TOTAL_ACADEMIC_STAFF = new MetricKey("total_academic_staff", "people");
        public static final MetricKey TOTAL_ADMINISTRATIVE_STAFF = new MetricKey("total_administrative_staff", "people");
        public static final MetricKey TOTAL_UNIVERSITY_BUDGET = new MetricKey("total_university_budget", "USD");
        public static final MetricKey SUSTAINABILITY_BUDGET = new MetricKey("sustainability_budget", "USD");
    }

    public static final class EnergyAndClimateChange {
        private EnergyAndClimateChange() {}
        public static final MetricKey ENERGY_EFFICIENT_APPLIANCES = new MetricKey("energy_efficient_appliances", "%");
        public static final MetricKey TOTAL_RENEWABLE_ENERGY_PRODUCTION_KWH = new MetricKey("total_renewable_energy_production_kwh", "kWh");
        public static final MetricKey TOTAL_CARBON_FOOTPRINT_TONS = new MetricKey("total_carbon_footprint_tons", "tons CO₂e");
    }

    public static final class Waste {
         private Waste() {}
         // Add Waste metric keys here if needed
    }

    public static final class Water {
        private Water() {}
        // Add Water metric keys here if needed
    }

    public static final class Transportation {
        private Transportation() {}
        public static final MetricKey GROUND_PARKING_AREA_M2 = new MetricKey("ground_parking_area_m2", "m²");
        public static final MetricKey INITIATIVES_TO_DECREASE_PRIVATE_VEHICLES = new MetricKey("initiatives_to_decrease_private_vehicles", "count");
    }

    public static final class EducationAndResearch {
        private EducationAndResearch() {}
        public static final MetricKey SUSTAINABILITY_COURSES = new MetricKey("sustainability_courses", "count");
        public static final MetricKey TOTAL_COURSES = new MetricKey("total_courses", "count");
        public static final MetricKey SUSTAINABILITY_RESEARCH_FUNDING = new MetricKey("sustainability_research_funding", "USD");
        public static final MetricKey TOTAL_RESEARCH_FUNDING = new MetricKey("total_research_funding", "USD");
        public static final MetricKey SUSTAINABILITY_PUBLICATIONS = new MetricKey("sustainability_publications", "count");
        public static final MetricKey SUSTAINABILITY_EVENTS = new MetricKey("sustainability_events", "count");
        public static final MetricKey STUDENT_SUSTAINABILITY_ACTIVITIES = new MetricKey("student_sustainability_activities", "count");
        public static final MetricKey CULTURAL_ACTIVITIES_COUNT = new MetricKey("cultural_activities_count", "count");
        public static final MetricKey INTERNATIONAL_COLLABORATION_PROGRAMS = new MetricKey("international_collaboration_programs", "count");
        public static final MetricKey COMMUNITY_SERVICE_PROGRAMS = new MetricKey("community_service_programs", "count");
        public static final MetricKey SUSTAINABILITY_STARTUPS = new MetricKey("sustainability_startups", "count");
        public static final MetricKey GRADUATES_IN_GREEN_JOBS = new MetricKey("graduates_in_green_jobs", "count");
        public static final MetricKey TOTAL_GRADUATES = new MetricKey("total_graduates", "count");
    }

    // NEW: Helper method to find a MetricKey by its string value
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
