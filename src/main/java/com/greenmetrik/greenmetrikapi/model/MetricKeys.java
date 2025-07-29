package com.greenmetrik.greenmetrikapi.model;

public final class MetricKeys {

    // Private constructor to prevent instantiation
    private MetricKeys() {}

    public static final class SettingAndInfrastructure {
        private SettingAndInfrastructure() {}
        public static final String TOTAL_CAMPUS_AREA_M2 = "total_campus_area_m2";
        public static final String OPEN_SPACE_AREA_M2 = "open_space_area_m2";
        public static final String FOREST_VEGETATION_AREA_M2 = "forest_vegetation_area_m2";
        public static final String PLANTED_VEGETATION_AREA_M2 = "planted_vegetation_area_m2";
        public static final String WATER_ABSORPTION_AREA_M2 = "water_absorption_area_m2";
        public static final String TOTAL_STUDENTS = "total_students";
        public static final String TOTAL_ACADEMIC_STAFF = "total_academic_staff";
        public static final String TOTAL_ADMINISTRATIVE_STAFF = "total_administrative_staff";
        public static final String TOTAL_UNIVERSITY_BUDGET = "total_university_budget";
        public static final String SUSTAINABILITY_BUDGET = "sustainability_budget";
    }

    public static final class EnergyAndClimateChange {
        private EnergyAndClimateChange() {}
        public static final String ENERGY_EFFICIENT_APPLIANCES = "energy_efficient_appliances";
        public static final String TOTAL_RENEWABLE_ENERGY_PRODUCTION_KWH = "total_renewable_energy_production_kwh";
        public static final String TOTAL_CARBON_FOOTPRINT_TONS = "total_carbon_footprint_tons";
    }

    public static final class Transportation {
        private Transportation() {}
        public static final String GROUND_PARKING_AREA_M2 = "ground_parking_area_m2";
        public static final String INITIATIVES_TO_DECREASE_PRIVATE_VEHICLES = "initiatives_to_decrease_private_vehicles";
    }

    public static final class EducationAndResearch {
        private EducationAndResearch() {}
        public static final String SUSTAINABILITY_COURSES = "sustainability_courses";
        public static final String TOTAL_COURSES = "total_courses";
        public static final String SUSTAINABILITY_RESEARCH_FUNDING = "sustainability_research_funding";
        public static final String TOTAL_RESEARCH_FUNDING = "total_research_funding";
        public static final String SUSTAINABILITY_PUBLICATIONS = "sustainability_publications";
        public static final String SUSTAINABILITY_EVENTS = "sustainability_events";
        public static final String STUDENT_SUSTAINABILITY_ACTIVITIES = "student_sustainability_activities";
        public static final String CULTURAL_ACTIVITIES_COUNT = "cultural_activities_count";
        public static final String INTERNATIONAL_COLLABORATION_PROGRAMS = "international_collaboration_programs";
        public static final String COMMUNITY_SERVICE_PROGRAMS = "community_service_programs";
        public static final String SUSTAINABILITY_STARTUPS = "sustainability_startups";
        public static final String GRADUATES_IN_GREEN_JOBS = "graduates_in_green_jobs";
        public static final String TOTAL_GRADUATES = "total_graduates";
    }
}
