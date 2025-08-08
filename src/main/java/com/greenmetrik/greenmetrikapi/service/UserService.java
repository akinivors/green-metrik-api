package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ChangePasswordRequest;
import com.greenmetrik.greenmetrikapi.dto.UserRegistrationRequest;
import com.greenmetrik.greenmetrikapi.dto.UserResponse;
import com.greenmetrik.greenmetrikapi.exception.DuplicateResourceException;
import com.greenmetrik.greenmetrikapi.exception.InvalidRequestException;
import com.greenmetrik.greenmetrikapi.exception.ResourceNotFoundException;
import com.greenmetrik.greenmetrikapi.model.Role;
import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService;

    public UserService(UserRepository userRepository, UnitRepository unitRepository, PasswordEncoder passwordEncoder, ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityLogService = activityLogService;
    }

    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new DuplicateResourceException(
                "Username already exists", "User", "username", request.username()
            );
        }
        validatePassword(request.password());

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Unit not found", "Unit", request.unitId()
                ));

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setFullName(request.fullName());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(Role.valueOf(request.role().toUpperCase()));
        newUser.setUnit(unit);
        newUser.setTemporaryPassword(true);

        User savedUser = userRepository.save(newUser);
        activityLogService.logActivity("USER_CREATED", "New user registered: " + savedUser.getUsername(), savedUser);

        return UserResponse.fromEntity(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "User", username));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidRequestException("Invalid old password");
        }

        validatePassword(request.newPassword());
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setTemporaryPassword(false);
        userRepository.save(user);
    }

    public UserResponse getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "User", username));
        return UserResponse.fromEntity(user);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new InvalidRequestException("Password must be at least 8 characters long.");
        }
    }

    public void deleteUser(Long id, String currentUsername) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "User", id));
        User adminUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found", "User", currentUsername));

        if (userToDelete.getUsername().equals(currentUsername)) {
            throw new InvalidRequestException("Admin users cannot delete their own accounts.");
        }

        userRepository.deleteById(id);
        activityLogService.logActivity("USER_DELETED", "Admin '" + adminUser.getUsername() + "' deleted user '" + userToDelete.getUsername() + "'", adminUser);
    }
}
