package org.developers.api.controllers.User;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetrics;
import org.developers.api.response.User.UserResponse;
import org.developers.service.impl.UserServiceImpl;
import org.developers.service.impl.ReCaptchaService;
import org.developers.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/v1/users"})
@Log4j2
public class UserController {

    private final UserServiceImpl userService;
    private final ReCaptchaService recaptchaService;

    @Autowired
    public UserController(UserServiceImpl userService, ReCaptchaService recaptchaService) {
        this.userService = userService;
        this.recaptchaService = recaptchaService;
    }

    @PostMapping({"/create"})
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUser request) {
        log.info("Creando usuario '{}'", request.getUsername());
        
        // Validar ReCaptcha
        if (!recaptchaService.verifyRecaptcha(request.getRecaptchaToken())) {
            log.warn("ReCaptcha validation failed for user '{}'", request.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // O crear una respuesta de error específica
        }
        
        return new ResponseEntity<>(this.userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping({"/credentials/find"})
    public ResponseEntity<UserResponse> getUserByCredentials(@RequestParam(name = "username") @Valid String request) {
        log.info("Buscando usuario con username: {}", request);
        GetUserByUserName credentials = new GetUserByUserName();
        credentials.setUserName(request);
        Optional<UserResponse> userByCredentials = userService.getUserByCredentials(credentials);
        return userByCredentials.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/email/find/{email}"})
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("Buscando usuario con email: {}", email);
        RetrieveUserByEmail request = new RetrieveUserByEmail();
        request.setEmail(email);
        Optional<UserResponse> userResponse = userService.retrieveUserByEmail(request);
        return userResponse.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/availability/username"})
    public ResponseEntity<Boolean> isUsernameAvailable(@RequestParam(name = "username") @Valid String requestUsername) {
        log.info("Buscando si el username '{}' ya existe para el usuario", requestUsername);
        IsUserNameAvailable request = new IsUserNameAvailable();
        request.setUserName(requestUsername);
        boolean usernameAvailable = userService.isUsernameAvailable(request);
        return usernameAvailable ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

    @GetMapping({"/availability/email"})
    public ResponseEntity<Boolean> isEmailAvailable(@RequestParam(name = "email") @Valid String request) {
        log.info("Buscando si el email '{}' ya existe", request);
        IsEmailAvailable emailRequest = new IsEmailAvailable();
        emailRequest.setEmail(request);
        boolean emailAvailable = userService.isEmailAvailable(emailRequest);
        return emailAvailable ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

    @GetMapping({"/activity/recent"})
    public ResponseEntity<List<UserResponse>> getRecentlyActiveUsers(@RequestParam(name = "sinceDate") @Valid String request) {
        log.info("Buscando usuarios activos recientemente desde la fecha '{}'", request);
        GetRecentlyActiveUsers sinceDate = new GetRecentlyActiveUsers();
        sinceDate.setSinceDate(LocalDateTime.parse(request));
        return ResponseEntity.ok(this.userService.getRecentlyActiveUsers(sinceDate));
    }

    @GetMapping({"/activity/most-engaged"})
    public ResponseEntity<List<UserResponse>> getMostEngagedUsers() {
        log.info("Obteniendo metricas del usuario");
        List<UserResponse> mostEngagedUsers = userService.getMostEngagedUsers();
        return mostEngagedUsers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(mostEngagedUsers);
    }

    @PostMapping({"/activity/record"})
    public ResponseEntity<Void> recordUserActivity(@RequestParam(name = "userId") @Valid Long userId) {
        log.info("Registrando actividad del usuario '{}'", userId);
        RecordUserActivity request = new RecordUserActivity();
        request.setUserId(userId);
        userService.recordUserActivity(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/metrics/get/{userId}"})
    public ResponseEntity<UserActivityMetrics> getUserActivityMetrics(@PathVariable Long userId) {
        log.info("Obteniendo metricas del usuario '{}'", userId);
        GetUserActivityMetrics request = new GetUserActivityMetrics();
        request.setUserId(userId);
        UserActivityMetrics userActivityMetrics = userService.getUserActivityMetrics(request);
        return userActivityMetrics == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(userActivityMetrics);
    }

    @PutMapping({"/update/{userId}"})
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @RequestBody @Valid UpdateUser request) {
        log.info("Actualizando datos del usuario '{}'", userId);
        UpdateUser.builder().id(userId).name(request.getName()).lastName(request.getLastName())
                .username(request.getUsername()).password(request.getPassword())
                .email(request.getEmail()).build();
        return ResponseEntity.ok(this.userService.updateUser(request));
    }

    @DeleteMapping({"/delete/user"})
    public ResponseEntity<Void> deleteUser(@RequestParam @Valid String email) {
        log.info("Eliminacion de usuario");
        DeleteUser deleteUserRequest = new DeleteUser();
        deleteUserRequest.setEmail(email);
        userService.deleteUser(deleteUserRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping({"/deactivate/inactive-users"})
    public ResponseEntity<Void> deactivateInactiveUsers(@RequestParam @Valid String thresholdDate) {
        log.info("Eliminacion de usuarios inactivos");
        DeactivateInactiveUsers request = new DeactivateInactiveUsers();
        request.setTresholdDate(LocalDateTime.parse(thresholdDate));
        this.userService.deactivateInactiveUsers(request);
        return ResponseEntity.ok().build();
    }
}
