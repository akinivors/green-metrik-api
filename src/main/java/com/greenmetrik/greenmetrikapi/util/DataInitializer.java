package com.greenmetrik.greenmetrikapi.util;

import com.greenmetrik.greenmetrikapi.model.*;
import com.greenmetrik.greenmetrikapi.repository.CampusMetricsRepository;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final PasswordEncoder passwordEncoder;
    private final CampusMetricsRepository campusMetricsRepository;

    public DataInitializer(UserRepository userRepository, UnitRepository unitRepository, PasswordEncoder passwordEncoder, CampusMetricsRepository campusMetricsRepository) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.passwordEncoder = passwordEncoder;
        this.campusMetricsRepository = campusMetricsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear all data to ensure a clean slate
        campusMetricsRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        unitRepository.deleteAllInBatch();

        // 1. Create the focused list of operational units
        Unit genelSekreterlik = new Unit();
        genelSekreterlik.setName("Genel Sekreterlik");

        Unit muhendislikFakultesi = new Unit();
        muhendislikFakultesi.setName("Mühendislik Fakültesi");

        Unit fenFakultesi = new Unit();
        fenFakultesi.setName("Fen Fakültesi");

        Unit mimarlikFakultesi = new Unit();
        mimarlikFakultesi.setName("Mimarlık Fakültesi");

        Unit yemekhane = new Unit();
        yemekhane.setName("Yemekhane");

        Unit guvenlikBirimi = new Unit();
        guvenlikBirimi.setName("Güvenlik Birimi");

        unitRepository.saveAll(List.of(genelSekreterlik, muhendislikFakultesi, fenFakultesi, mimarlikFakultesi, yemekhane, guvenlikBirimi));

        // 2. Create the default admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Administrator");
        admin.setRole(Role.ADMIN);
        admin.setUnit(genelSekreterlik); // Assign to the correct unit
        admin.setTemporaryPassword(false);
        userRepository.save(admin);

        // 3. Create a sample metric for every key defined in MetricKeys.java
        createAllSampleMetrics();

        System.out.println("Database has been re-initialized with operational units, admin user, and a full set of sample metrics.");
    }

    private void createAllSampleMetrics() {
        List<CampusMetrics> metricsToSave = new ArrayList<>();

        Stream.of(
            MetricKeys.SettingAndInfrastructure.class.getFields(),
            MetricKeys.EnergyAndClimateChange.class.getFields(),
            MetricKeys.Transportation.class.getFields(),
            MetricKeys.EducationAndResearch.class.getFields()
        )
        .flatMap(Arrays::stream)
        .filter(field -> field.getType().equals(MetricKeys.MetricKey.class))
        .forEach(field -> {
            try {
                MetricKeys.MetricKey keyInfo = (MetricKeys.MetricKey) field.get(null);
                CampusMetrics metric = new CampusMetrics();
                metric.setMetricKey(keyInfo.key());
                metric.setMetricValue("100"); // Default sample value
                metric.setMetricDate(LocalDate.of(2024, 1, 1));
                // Determine category from the class name
                String categoryName = field.getDeclaringClass().getSimpleName().toUpperCase();
                if(categoryName.equals("SETTINGANDINFRASTRUCTURE")) categoryName = "SETTING_INFRASTRUCTURE";
                if(categoryName.equals("ENERGYANDCLIMATECHANGE")) categoryName = "ENERGY_CLIMATE_CHANGE";
                if(categoryName.equals("EDUCATIONANDRESEARCH")) categoryName = "EDUCATION_RESEARCH";

                metric.setCategory(MetricCategory.valueOf(categoryName));
                metric.setDescription("Sample data for " + keyInfo.key());
                metricsToSave.add(metric);
            } catch (IllegalAccessException e) {
                // This should not happen for public static fields
                throw new RuntimeException("Could not access metric key field", e);
            }
        });

        campusMetricsRepository.saveAll(metricsToSave);
    }
}
