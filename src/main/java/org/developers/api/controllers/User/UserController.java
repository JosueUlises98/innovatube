package org.developers.api.controllers.User;

import jakarta.validation.Valid;
import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetricsResponse;
import org.developers.api.response.User.UserResponse;
import org.developers.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        logger.info("Creando nuevo usuario con username: {}", request.getUsername());
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping("/credentials")
    public ResponseEntity<UserResponse> getUserByCredentials(@Valid @RequestBody GetUserByUserNameRequest request) {
        return userService.getUserByCredentials(request)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        RetrieveUserByEmailRequest request = new RetrieveUserByEmailRequest();
        request.setEmail(email);
        return userService.retrieveUserByEmail(request)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/check/username")
    public ResponseEntity<Boolean> isUsernameAvailable(@Valid @RequestBody IsUserNameAvaibleRequest request) {
        return ResponseEntity.ok(userService.isUsernameAvailable(request));
    }

    @GetMapping("/check/email")
    public ResponseEntity<Boolean> isEmailAvailable(@Valid @RequestBody IsEmailAvaibleRequest request) {
        return ResponseEntity.ok(userService.isEmailAvailable(request));
    }

    @GetMapping("/active/recent")
    public ResponseEntity<List<UserResponse>> getRecentlyActiveUsers(@Valid @RequestBody GetRecentlyActiveUsersRequest request) {
        return ResponseEntity.ok(userService.getRecentlyActiveUsers(request));
    }

    @GetMapping("/most-engaged")
    public ResponseEntity<List<UserResponse>> getMostEngagedUsers() {
        return ResponseEntity.ok(userService.getMostEngagedUsers());
    }

    @PostMapping("/activity")
    public ResponseEntity<Void> recordUserActivity(@Valid @RequestBody RecordUserActivityRequest request) {
        userService.recordUserActivity(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics/{userId}")
    public ResponseEntity<UserActivityMetricsResponse> getUserActivityMetrics(@PathVariable Long userId) {
        GetUserActivityMetricsRequest request = new GetUserActivityMetricsRequest();
        request.setUserId(userId);
        return ResponseEntity.ok(userService.getUserActivityMetrics(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request) {
        UpdateUserRequest.builder()
                .id(userId)
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .build();

        return ResponseEntity.ok(userService.updateUser(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        userService.deleteUser(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deactivate-inactive")
    public ResponseEntity<Void> deactivateInactiveUsers(@Valid @RequestBody DeactivateInactiveUsersRequest request) {
        userService.deactivateInactiveUsers(request);
        return ResponseEntity.ok().build();
    }
}
