package com.greenmetrik.greenmetrikapi.util;

import com.greenmetrik.greenmetrikapi.model.*;
import com.greenmetrik.greenmetrikapi.repository.*;
import com.greenmetrik.greenmetrikapi.service.ActivityLogService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final PasswordEncoder passwordEncoder;
    private final CampusMetricsRepository campusMetricsRepository;
    private final ActivityLogRepository activityLogRepository;
    private final ElectricityConsumptionRepository electricityRepository;
    private final WaterConsumptionRepository waterRepository;
    private final WasteDataRepository wasteDataRepository;
    private final VehicleEntryRepository vehicleEntryRepository;
    private final ActivityLogService activityLogService;

    public DataInitializer(UserRepository userRepository, UnitRepository unitRepository, PasswordEncoder passwordEncoder, CampusMetricsRepository campusMetricsRepository, ActivityLogRepository activityLogRepository, ElectricityConsumptionRepository electricityRepository, WaterConsumptionRepository waterRepository, WasteDataRepository wasteDataRepository, VehicleEntryRepository vehicleEntryRepository, ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.passwordEncoder = passwordEncoder;
        this.campusMetricsRepository = campusMetricsRepository;
        this.activityLogRepository = activityLogRepository;
        this.electricityRepository = electricityRepository;
        this.waterRepository = waterRepository;
        this.wasteDataRepository = wasteDataRepository;
        this.vehicleEntryRepository = vehicleEntryRepository;
        this.activityLogService = activityLogService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // This check from your coworker makes the seeder non-destructive.
        if (unitRepository.count() == 0 && userRepository.count() == 0) {
            System.out.println("Database is empty. Initializing with sample data...");

            // 1. Create units (Using correct pattern with default constructor + setter)
            Unit genelSekreterlik = new Unit();
            genelSekreterlik.setName("Genel Sekreterlik");

            Unit yemekhaneUnit = new Unit();
            yemekhaneUnit.setName("Yemekhane");

            Unit muhendislikFakultesi = new Unit();
            muhendislikFakultesi.setName("Mühendislik Fakültesi");

            Unit guvenlikUnit = new Unit();
            guvenlikUnit.setName("Güvenlik Birimi");

            unitRepository.saveAll(List.of(genelSekreterlik, yemekhaneUnit, muhendislikFakultesi, guvenlikUnit));

            // 2. Create users
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setRole(Role.ADMIN);
            admin.setUnit(genelSekreterlik);
            admin.setTemporaryPassword(false);

            User yemekhaneUser = new User();
            yemekhaneUser.setUsername("yemekhane");
            yemekhaneUser.setPassword(passwordEncoder.encode("yemekhane123"));
            yemekhaneUser.setFullName("Yemekhane Staff");
            yemekhaneUser.setRole(Role.YEMEKHANE);
            yemekhaneUser.setUnit(yemekhaneUnit);
            yemekhaneUser.setTemporaryPassword(false);

            User binaGorevlisi = new User();
            binaGorevlisi.setUsername("bina_gorevlisi");
            binaGorevlisi.setPassword(passwordEncoder.encode("bina123"));
            binaGorevlisi.setFullName("Bina Görevlisi");
            binaGorevlisi.setRole(Role.BINA_GOREVLISI);
            binaGorevlisi.setUnit(muhendislikFakultesi);
            binaGorevlisi.setTemporaryPassword(false);

            User guvenlikUser = new User();
            guvenlikUser.setUsername("guvenlik");
            guvenlikUser.setPassword(passwordEncoder.encode("guvenlik123"));
            guvenlikUser.setFullName("Güvenlik Personeli");
            guvenlikUser.setRole(Role.GUVENLIK);
            guvenlikUser.setUnit(guvenlikUnit);
            guvenlikUser.setTemporaryPassword(false);
            userRepository.saveAll(List.of(admin, yemekhaneUser, binaGorevlisi, guvenlikUser));

            // 3. Create static metrics
            createAllSampleMetrics();

            // 4. Create dynamic sample waste data
            createSampleWasteData(yemekhaneUser);

            // 5. Create dynamic sample consumption data
            createSampleConsumptionData(binaGorevlisi, muhendislikFakultesi);

            // 6. Create dynamic sample vehicle data
            createSampleVehicleData(guvenlikUser);

            System.out.println("Database has been initialized with all sample data.");
        }
    }

    private void createAllSampleMetrics() {
        List<CampusMetrics> metricsToSave = new ArrayList<>();
        LocalDate sampleDate = LocalDate.of(2024, 1, 1);

        TriConsumer<Class<?>, MetricCategory, List<CampusMetrics>> processCategory =
            (categoryClass, categoryEnum, list) -> {
                Arrays.stream(categoryClass.getFields())
                    .filter(field -> field.getType().equals(MetricKeys.MetricKey.class))
                    .forEach(field -> {
                        try {
                            MetricKeys.MetricKey keyInfo = (MetricKeys.MetricKey) field.get(null);
                            CampusMetrics metric = new CampusMetrics();
                            metric.setMetricKey(keyInfo.key());
                            metric.setMetricDate(sampleDate);
                            metric.setDescription("Sample data for " + keyInfo.key());
                            metric.setCategory(categoryEnum);
                            metric.setMetricValue(getRealisticValueForKey(keyInfo.key()));
                            list.add(metric);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Could not access metric key field: " + field.getName(), e);
                        }
                    });
            };

        processCategory.accept(MetricKeys.SettingAndInfrastructure.class, MetricCategory.SETTING_INFRASTRUCTURE, metricsToSave);
        processCategory.accept(MetricKeys.EnergyAndClimateChange.class, MetricCategory.ENERGY_CLIMATE_CHANGE, metricsToSave);
        processCategory.accept(MetricKeys.Waste.class, MetricCategory.WASTE, metricsToSave);
        processCategory.accept(MetricKeys.Water.class, MetricCategory.WATER, metricsToSave);
        processCategory.accept(MetricKeys.Transportation.class, MetricCategory.TRANSPORTATION, metricsToSave);
        processCategory.accept(MetricKeys.EducationAndResearch.class, MetricCategory.EDUCATION_RESEARCH, metricsToSave);

        campusMetricsRepository.saveAll(metricsToSave);
        System.out.println("Generated " + metricsToSave.size() + " realistic sample static campus metrics.");
    }

    private String getRealisticValueForKey(String key) {
        switch (key) {
            case "institution_type": return "Comprehensive";
            case "climate_zone": return "Humid Subtropical";
            case "campus_setting": return "Urban";
            case "num_of_campus_sites": return "1";
            case "ict_infrastructure_program_status": return "Implemented & Evaluated";
            case "total_campus_area_m2": return "500000";
            case "open_space_area_m2": return "350000";
            case "forest_vegetation_area_m2": return "50000";
            case "planted_vegetation_area_m2": return "150000";
            case "water_absorption_area_m2": return "100000";
            case "total_students": return "15000";
            case "total_academic_staff": return "1200";
            case "total_administrative_staff": return "800";
            case "total_university_budget": return "50000000";
            case "sustainability_budget": return "750000";
            case "conservation_program_implementation_percent": return "60";
            case "energy_efficient_appliances": return "85";
            case "total_renewable_energy_production_kwh": return "300000";
            case "total_carbon_footprint_tons": return "5000";
            case "smart_building_area_m2": return "40000";
            case "total_building_area_m2": return "150000";
            case "renewable_energy_source_count": return "3";
            case "green_building_elements_count": return "5";
            case "innovative_energy_programs_count": return "2";
            case "paper_plastic_reduction_programs_count": return "5";
            case "sewage_treatment_level": return "TERTIARY";
            case "water_conservation_percentage": return "15";
            case "water_efficient_appliance_percentage": return "75";
            case "ground_parking_area_m2": return "25000";
            case "initiatives_to_decrease_private_vehicles": return "4";
            case "sustainability_courses": return "80";
            case "total_courses": return "1600";
            case "sustainability_research_funding": return "500000";
            case "total_research_funding": return "2500000";
            case "sustainability_publications": return "150";
            case "sustainability_events": return "25";
            case "student_sustainability_activities": return "40";
            case "cultural_activities_count": return "15";
            case "international_collaboration_programs": return "8";
            case "community_service_programs": return "22";
            case "sustainability_startups": return "5";
            case "graduates_in_green_jobs": return "300";
            case "total_graduates": return "2000";
            default: return "0";
        }
    }

    @FunctionalInterface
    interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

    private void createSampleWasteData(User user) {
        List<WasteData> wasteEntries = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 365; i++) {
            LocalDate date = today.minusDays(i);
            WasteData wasteData = new WasteData();
            wasteData.setDataDate(date);
            wasteData.setUser(user);
            double organicProd = ThreadLocalRandom.current().nextDouble(150, 250);
            wasteData.setOrganicProductionKg(organicProd);
            wasteData.setOrganicConsumptionKg(organicProd * 0.95);
            wasteData.setOrganicTreatedKg(organicProd * 0.9);
            double inorganicProd = ThreadLocalRandom.current().nextDouble(80, 120);
            wasteData.setInorganicProductionKg(inorganicProd);
            wasteData.setInorganicConsumptionKg(inorganicProd * 0.85);
            wasteData.setInorganicRecycledKg(inorganicProd * 0.7);
            wasteData.setToxicWasteKg(ThreadLocalRandom.current().nextDouble(1, 5));
            wasteData.setTreatedToxicWasteKg(ThreadLocalRandom.current().nextDouble(0, 1));
            wasteData.setSewageDisposalLiters(ThreadLocalRandom.current().nextDouble(1000, 2000));
            wasteEntries.add(wasteData);
        }
        wasteDataRepository.saveAll(wasteEntries);
        System.out.println("Generated " + wasteEntries.size() + " sample waste data entries.");

        // NEW: Add activity logging for each created entry
        for (WasteData wasteData : wasteEntries) {
            activityLogService.logActivity(
                "WASTE_DATA_CREATED",
                "Sample waste data created for date: " + wasteData.getDataDate(),
                user
            );
        }
    }

    private void createSampleConsumptionData(User user, Unit unit) {
        List<ElectricityConsumption> electricityEntries = new ArrayList<>();
        List<WaterConsumption> waterEntries = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 12; i++) {
            LocalDate endDate = today.minusMonths(i);
            LocalDate startDate = endDate.withDayOfMonth(1);
            ElectricityConsumption elec = new ElectricityConsumption();
            elec.setPeriodStartDate(startDate);
            elec.setPeriodEndDate(endDate);
            elec.setConsumptionKwh(ThreadLocalRandom.current().nextDouble(20000, 28000));
            elec.setUnit(unit);
            elec.setUser(user);
            electricityEntries.add(elec);
            WaterConsumption water = new WaterConsumption();
            water.setPeriodStartDate(startDate);
            water.setPeriodEndDate(endDate);
            water.setConsumptionTon(ThreadLocalRandom.current().nextDouble(4000, 5000));
            water.setRecycledWaterUsageLiters(water.getConsumptionTon() * 100);
            water.setTreatedWaterConsumptionLiters(water.getConsumptionTon() * 250);
            water.setUnit(unit);
            water.setUser(user);
            waterEntries.add(water);
        }
        electricityRepository.saveAll(electricityEntries);
        waterRepository.saveAll(waterEntries);
        System.out.println("Generated " + electricityEntries.size() + " sample electricity and water entries.");

        // NEW: Add activity logging for each created entry
        for (ElectricityConsumption elec : electricityEntries) {
            activityLogService.logActivity(
                "ELECTRICITY_LOG_CREATED",
                "Sample electricity consumption log created for unit: " + unit.getName(),
                user
            );
        }
        for (WaterConsumption water : waterEntries) {
            activityLogService.logActivity(
                "WATER_LOG_CREATED",
                "Sample water consumption log created for unit: " + unit.getName(),
                user
            );
        }
    }

    private void createSampleVehicleData(User user) {
        List<VehicleEntry> vehicleEntries = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 365; i++) {
            LocalDate date = today.minusDays(i);
            VehicleEntry entry = new VehicleEntry();
            entry.setEntryDate(date);
            entry.setUser(user);
            entry.setPublicTransportCount(ThreadLocalRandom.current().nextInt(100, 151));
            entry.setPrivateVehicleCount(ThreadLocalRandom.current().nextInt(700, 1001));
            entry.setMotorcycleCount(ThreadLocalRandom.current().nextInt(200, 301));
            entry.setZevCount(ThreadLocalRandom.current().nextInt(20, 51));
            vehicleEntries.add(entry);
        }
        vehicleEntryRepository.saveAll(vehicleEntries);
        System.out.println("Generated " + vehicleEntries.size() + " sample vehicle data entries.");

        // NEW: Add activity logging for each created entry
        for (VehicleEntry entry : vehicleEntries) {
            activityLogService.logActivity(
                "VEHICLE_ENTRY_CREATED",
                "Sample vehicle entry created for date: " + entry.getEntryDate(),
                user
            );
        }
    }
}