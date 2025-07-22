package com.greenmetrik.greenmetrikapi.util;

import com.greenmetrik.greenmetrikapi.model.Role;
import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, UnitRepository unitRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if there are any units. If so, don't add more.
        if (unitRepository.count() == 0) {
            // 1. Create the Admin Unit
            Unit adminUnit = new Unit();
            adminUnit.setName("Admin Unit");
            unitRepository.save(adminUnit);

            // 2. Create sample building units
            Unit engineeringBuilding = new Unit();
            engineeringBuilding.setName("Engineering Building");

            Unit scienceBuilding = new Unit();
            scienceBuilding.setName("Science Building");

            unitRepository.saveAll(List.of(engineeringBuilding, scienceBuilding));

            // 3. Create the admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setRole(Role.ADMIN);
            admin.setUnit(adminUnit);
            userRepository.save(admin);

            System.out.println("Database seeded with admin user and sample units.");
        }
    }
}
