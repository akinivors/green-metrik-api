package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.UserRegistrationRequest;
import com.greenmetrik.greenmetrikapi.dto.UserResponse;
import com.greenmetrik.greenmetrikapi.model.Role;
import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UnitRepository unitRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setFullName(request.fullName());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(Role.valueOf(request.role().toUpperCase()));
        newUser.setUnit(unit);
        newUser.setTemporaryPassword(true); // Setting default temporary password flag

        User savedUser = userRepository.save(newUser);
        return UserResponse.fromEntity(savedUser);
    }
}
