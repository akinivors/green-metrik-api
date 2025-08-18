package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ChangePasswordRequest;
import com.greenmetrik.greenmetrikapi.dto.UserRegistrationRequest;
import com.greenmetrik.greenmetrikapi.dto.UserResponse;
import com.greenmetrik.greenmetrikapi.dto.UserUpdateRequest;
import com.greenmetrik.greenmetrikapi.exception.DuplicateResourceException;
import com.greenmetrik.greenmetrikapi.exception.InvalidRequestException;
import com.greenmetrik.greenmetrikapi.exception.ResourceNotFoundException;
import com.greenmetrik.greenmetrikapi.model.Role;
import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.UnitRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.util.RepositoryHelper; // Import the new helper
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        Unit unit = RepositoryHelper.findOrThrow(unitRepository, request.unitId(), "Unit");

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setFullName(request.fullName());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(Role.valueOf(request.role().toUpperCase()));
        newUser.setUnit(unit);
        newUser.setTemporaryPassword(true);

        User savedUser = userRepository.save(newUser);
        activityLogService.logActivity("CREATED", "USER", "New user registered: " + savedUser.getUsername(), savedUser);

        return UserResponse.fromEntity(savedUser);
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            Sort.by("id").descending()
        );

        return userRepository.findAllActiveUsers(sortedPageable)
                .map(UserResponse::fromEntity);
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findActiveByUsername(username)
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
        User user = userRepository.findActiveByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "User", username));
        return UserResponse.fromEntity(user);
    }

    public UserResponse getUserById(Long id) {
        User user = RepositoryHelper.findOrThrow(userRepository, id, "User");
        return UserResponse.fromEntity(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User userToUpdate = RepositoryHelper.findOrThrow(userRepository, id, "User");
        Unit unit = RepositoryHelper.findOrThrow(unitRepository, request.unitId(), "Unit");

        userToUpdate.setFullName(request.fullName());
        userToUpdate.setRole(Role.valueOf(request.role().toUpperCase()));
        userToUpdate.setUnit(unit);

        User updatedUser = userRepository.save(userToUpdate);
        activityLogService.logActivity("UPDATED", "USER", "Admin updated details for user: " + updatedUser.getUsername(), updatedUser);

        return UserResponse.fromEntity(updatedUser);
    }

    public String resetPassword(Long id, String adminUsername) {
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found", "User", adminUsername));

        User userToReset = RepositoryHelper.findOrThrow(userRepository, id, "User");

        if (userToReset.getUsername().equals(adminUsername)) {
            throw new InvalidRequestException("Admins cannot reset their own password via this method.");
        }

        String temporaryPassword = generateSecurePassword();

        userToReset.setPassword(passwordEncoder.encode(temporaryPassword));
        userToReset.setTemporaryPassword(true);
        userRepository.save(userToReset);

        activityLogService.logActivity(
            "RESET_PASSWORD", "USER",
            "Admin '" + adminUser.getUsername() + "' reset password for user '" + userToReset.getUsername() + "'",
            adminUser
        );

        return temporaryPassword;
    }

    private String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 12)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new InvalidRequestException("Password must be at least 8 characters long.");
        }
    }

    public void deleteUser(Long id, String currentUsername) {
        User adminUser = userRepository.findActiveByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Performing admin user not found", "User", currentUsername));

        User userToDeactivate = RepositoryHelper.findOrThrow(userRepository, id, "User");

        if (userToDeactivate.getUsername().equals(currentUsername)) {
            throw new InvalidRequestException("Admin users cannot delete their own accounts.");
        }

        userToDeactivate.setDeleted(true);
        userRepository.save(userToDeactivate);

        activityLogService.logActivity(
            "DELETED", "USER",
            "Admin '" + adminUser.getUsername() + "' deactivated user '" + userToDeactivate.getUsername() + "'",
            adminUser
        );
    }
}
