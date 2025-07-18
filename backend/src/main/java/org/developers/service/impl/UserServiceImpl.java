package org.developers.service.impl;

import lombok.extern.log4j.Log4j2;
import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetrics;
import org.developers.api.response.User.UserResponse;
import org.developers.api.response.User.UserSessionDetails;
import org.developers.common.exception.exceptions.AuthenticationException;
import org.developers.model.entities.User;
import org.developers.model.mapper.UserMapper;
import org.developers.repository.UserRepository;
import org.developers.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSessionDetails login(Login loginRequest) {
        log.info("Intentando autenticar usuario con username: {}", loginRequest.getUsername());
        Optional<User> userOptional = this.userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            log.warn("Intento de login fallido: Usuario '{}' no encontrado.", loginRequest.getUsername());
            throw new AuthenticationException("Credenciales incorrectas.");
        } else {
            User user = userOptional.get();
            if (this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.info("Autenticación exitosa para el usuario: {}", loginRequest.getUsername());
                UserSessionDetails detailsResponse = new UserSessionDetails();
                detailsResponse.setUserId(user.getUserid());
                detailsResponse.setEmail(user.getEmail());
                detailsResponse.setUsername(user.getUsername());
                return detailsResponse;
            } else {
                log.warn("Intento de login fallido: Contraseña incorrecta para el usuario '{}'.", loginRequest.getUsername());
                throw new AuthenticationException("Credenciales incorrectas.");
            }
        }
    }

    public UserSessionDetails getUserById(Long userId) {
        Optional<User>var10000 = this.userRepository.findById(userId);
        UserMapper var10001 = this.userMapper;
        Objects.requireNonNull(var10001);
        return var10000.map(var10001::toUserSessionDetails).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Optional<UserResponse> getUserByCredentials(GetUserByUserName request) {
        log.info("Buscando usuario por credenciales: {}", request.getUserName());
        Optional<User> user = this.userRepository.findByUsername(request.getUserName());
        if (user.isEmpty()) {
            log.info("Usuario no encontrado con credenciales: {}", request.getUserName());
            return Optional.empty();
        } else {
            return user.map((user1) -> UserResponse.builder().name(user1.getName()).lastname(user1.getLastname()).username(user1.getUsername()).email(user1.getEmail()).createdAt(user1.getCreatedAt()).lastLogin(user1.getLastLogin()).isActive(user1.getIsActive()).build());
        }
    }

    public Optional<UserResponse> retrieveUserByEmail(RetrieveUserByEmail request) {
        log.info("Buscando usuario por email: {}", request.getEmail());
        Optional<User> var10000 = this.userRepository.findByEmail(request.getEmail());
        UserMapper var10001 = this.userMapper;
        Objects.requireNonNull(var10001);
        return var10000.map(var10001::toUserResponse);
    }

    public boolean isUsernameAvailable(IsUserNameAvailable request) {
        log.debug("Verificando disponibilidad del username: {}", request.getUserName());
        // Retorna true si el username NO existe (está disponible)
        return !this.userRepository.existsByUsername(request.getUserName());
    }

    public boolean isEmailAvailable(IsEmailAvailable request) {
        log.debug("Verificando disponibilidad del email: {}", request.getEmail());
        // Retorna true si el email NO existe (está disponible)
        return !this.userRepository.existsByEmail(request.getEmail());
    }

    public boolean isUserNameAvailableById(IsUserNameAvailable userNameAvaibleRequest) {
        log.debug("Verificando disponibilidad del username por id: {}", userNameAvaibleRequest.getUserId());
        return this.userRepository.existsById(userNameAvaibleRequest.getUserId());
    }

    public List<UserResponse> getRecentlyActiveUsers(GetRecentlyActiveUsers request) {
        log.info("Obteniendo usuarios activos desde: {}", request.getSinceDate());
        List<User> var10000 = userRepository.findByLastLoginAfter(request.getSinceDate());
        return var10000.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public List<UserResponse> getUsersRegisteredInPeriod(GetUsersRegisteredInPeriod request) {
        log.info("Buscando usuarios registrados entre {} y {}", request.getStartDate(), request.getEndDate());
        List<User> var10000 = userRepository.findByCreatedAtBetween(LocalDateTime.parse(request.getStartDate()),LocalDateTime.parse(request.getEndDate()));
        return var10000.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public List<UserResponse> getInactiveUsersSince(GetInactiveUsersSince request) {
        log.info("Buscando usuarios inactivos desde: {}", request.getDate());
        List<User> var10000 = userRepository.findInactiveUsers(request.getDate());
        return var10000.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public List<UserResponse> getMostEngagedUsers() {
        log.info("Obteniendo usuarios más activos");
        List<User> var10000 = this.userRepository.findMostActiveUsers(Pageable.unpaged());
        return var10000.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public void recordUserActivity(RecordUserActivity request) {
        log.debug("Registrando actividad para usuario ID: {}", request.getUserId());
        this.userRepository.findById(request.getUserId()).ifPresent((user) -> {
            user.setLastLogin(LocalDateTime.now());
            this.userRepository.save(user);
        });
    }

    public void updateUserLastSession(UpdateUserLastSession request) {
        log.debug("Actualizando última sesión para usuario ID: {}", request.getUserId());
        this.userRepository.findById(request.getUserId()).ifPresent((user) -> {
            user.setLastLogin(LocalDateTime.now().minusHours(2L));
            this.userRepository.save(user);
        });
    }

    public UserActivityMetrics getUserActivityMetrics(GetUserActivityMetrics request) {
        log.info("Obteniendo métricas de actividad para usuario ID: {}", request.getUserId());
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30L);
        return this.userRepository.findUserMetrics(request.getUserId(), thirtyDaysAgo);
    }

    public void deactivateInactiveUsers(DeactivateInactiveUsers request) {
        log.info("Desactivando usuarios inactivos desde: {}", request.getTresholdDate());
        List<User> inactiveUsers = this.userRepository.findInactiveUsers(request.getTresholdDate());
        inactiveUsers.forEach((user) -> {
            user.setIsActive(false);
            this.userRepository.save(user);
        });
        log.info("Se desactivaron {} usuarios", inactiveUsers.size());
    }

    public boolean isUserActive(IsUserActive request) {
        log.debug("Verificando si el usuario ID: {} está activo", request.getUserId());
        return userRepository.findById(request.getUserId()).map(User::getIsActive).orElse(false);
    }

    @Transactional
    public UserResponse updateUser(UpdateUser request) {
        log.info("Actualizando usuario ID: {}", request.getId());
        return userRepository.findById(request.getId()).map((existingUser) -> {
            String encoded = passwordEncoder.encode(request.getPassword());
            userMapper.updateEntity(existingUser, UserResponse.builder().name(request.getName())
                    .lastname(request.getLastName())
                    .username(request.getUsername())
                    .password(encoded != null ? encoded : existingUser.getPassword())
                    .email(request.getEmail()).createdAt(existingUser.getCreatedAt())
                    .lastLogin(existingUser.getLastLogin()).isActive(existingUser.getIsActive()).build());
            User savedUser = userRepository.save(existingUser);
            log.info("Usuario actualizado: {}", savedUser);
            return this.userMapper.toUserResponse(savedUser);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public void deleteUser(DeleteUser request) {
        AtomicReference<User> loguser = new AtomicReference();
        log.info("Eliminando usuario con email: {}", request.getEmail());
        this.userRepository.findByEmail(request.getEmail()).ifPresent((user) -> {
            user.setIsActive(false);
            this.userRepository.delete(user);
            loguser.set(user);
        });
        log.info("Usuario eliminado: {}", loguser.get() != null ? (loguser.get()).getUsername() : "No encontrado");
    }

    @Transactional
    public UserResponse createUser(CreateUser request) {
        log.info("Creando usuario '{}'", request.getUsername());
        String encode = passwordEncoder.encode(request.getPassword());
        UserResponse userResponse = UserResponse.builder().name(request.getName())
                .lastname(request.getLastname()).username(request.getUsername())
                .password(encode).email(request.getEmail()).createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now()).isActive(true).build();
        User user = userMapper.toEntity(userResponse);
        User savedUser = userRepository.save(user);
        log.info("Usuario creado: {}", savedUser);
        return userMapper.toUserResponse(savedUser);
    }

    // New method implementations for ProfileController
    @Transactional
    public UserResponse updateUserProfile(UpdateUserProfile request) {
        log.info("Actualizando perfil de usuario ID: {}", request.getUserId());
        return userRepository.findById(request.getUserId()).map(existingUser -> {
            existingUser.setName(request.getFirstName());
            existingUser.setLastname(request.getLastName());
            // Note: email is not in UpdateUserProfile, so we'll keep the existing email
            User savedUser = userRepository.save(existingUser);
            return userMapper.toUserResponse(savedUser);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public void deleteUserById(Long userId) {
        log.info("Eliminando usuario ID: {}", userId);
        userRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(false);
            userRepository.delete(user);
            log.info("Usuario eliminado: {} exitosamente", user.getUsername());
        });
    }

    public UserActivityMetrics getUserActivityMetricsById(Long userId) {
        log.info("Obteniendo métricas de actividad para usuario ID.: {}", userId);
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30L);
        return userRepository.findUserMetrics(userId, thirtyDaysAgo);
    }

    public List<UserSessionDetails> getUserSessions(Long userId) {
        log.info("Obteniendo sesiones de usuario ID: {}", userId);
        // This is a placeholder implementation - you would need to implement session tracking
        return List.of();
    }

    @Transactional
    public UserResponse updateUserAvatar(UpdateUserAvatar request) {
        log.info("Actualizando avatar de usuario ID: {}", request.getUserId());
        return userRepository.findById(request.getUserId()).map(existingUser -> {
            // In a real implementation, you would save the avatar file and store the path
            // existingUser.setAvatarUrl(request.getAvatarUrl());
            User savedUser = userRepository.save(existingUser);
            return userMapper.toUserResponse(savedUser);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public void changePassword(ChangePassword request) {
        log.info("Cambiando contraseña para usuario ID: {}", request.getUserId());
        userRepository.findById(request.getUserId()).ifPresent(user -> {
            if (passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
                user.setPassword(newEncodedPassword);
                userRepository.save(user);
                log.info("Contraseña actualizada para usuario: {}", user.getUsername());
            } else {
                throw new RuntimeException("Contraseña actual incorrecta");
            }
        });
    }

    @Transactional
    public UserResponse updateEmail(UpdateEmail request) {
        log.info("Actualizando email de usuario ID: {}", request.getUserId());
        return userRepository.findById(request.getUserId()).map(existingUser -> {
            existingUser.setEmail(request.getNewEmail());
            User savedUser = userRepository.save(existingUser);
            return userMapper.toUserResponse(savedUser);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public UserResponse updateUsername(UpdateUsername request) {
        log.info("Actualizando username de usuario ID: {}", request.getUserId());
        return userRepository.findById(request.getUserId()).map(existingUser -> {
            existingUser.setUsername(request.getUsername());
            User savedUser = userRepository.save(existingUser);
            return userMapper.toUserResponse(savedUser);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Map<String, Object> getUserPreferences(Long userId) {
        log.info("Obteniendo preferencias de usuario ID: {}", userId);
        // This is a placeholder implementation
        return Map.of("theme", "dark", "language", "es", "notifications", true);
    }

    @Transactional
    public void updateUserPreferences(Long userId, Map<String, Object> preferences) {
        log.info("Actualizando preferencias de usuario ID: {}", userId);
        // This is a placeholder implementation - you would need to implement preference storage
    }

    public Map<String, Object> getUserStatistics(Long userId) {
        log.info("Obteniendo estadísticas de usuario ID: {}", userId);
        // This is a placeholder implementation
        return Map.of(
            "totalVideos", 0L,
            "totalFavorites", 0L,
            "totalViews", 0L,
            "lastActivity", LocalDateTime.now().toString()
        );
    }

    @Transactional
    public void deactivateUser(Long userId) {
        log.info("Desactivando usuario ID: {}", userId);
        userRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(false);
            userRepository.save(user);
            log.info("Usuario desactivado: {}", user.getUsername());
        });
    }

    @Transactional
    public void activateUser(Long userId) {
        log.info("Activando usuario ID: {}", userId);
        userRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(true);
            userRepository.save(user);
            log.info("Usuario activado: {}", user.getUsername());
        });
    }

    public Long getUserFavoritesCount(Long userId) {
        log.info("Obteniendo conteo de favoritos para usuario ID: {}", userId);
        // This is a placeholder implementation - you would need to implement favorites counting
        return 0L;
    }

    public Long getUserVideosCount(Long userId) {
        log.info("Obteniendo conteo de videos para usuario ID: {}", userId);
        // This is a placeholder implementation - you would need to implement videos counting
        return 0L;
    }

    public List<Map<String, Object>> getUserRecentActivity(Long userId) {
        log.info("Obteniendo actividad reciente de usuario ID: {}", userId);
        // This is a placeholder implementation
        return List.of(Map.of(
            "type", "video_view",
            "timestamp", LocalDateTime.now().toString(),
            "description", "Vio un video"
        ));
    }

    public Map<String, Object> exportUserData(Long userId) {
        log.info("Exportando datos de usuario ID: {}", userId);
        return userRepository.findById(userId).map(user -> {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUserid());
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("name", user.getName());
            data.put("lastname", user.getLastname());
            data.put("createdAt", user.getCreatedAt().toString());
            data.put("lastLogin", user.getLastLogin() != null ? user.getLastLogin().toString() : null);
            data.put("isActive", user.getIsActive());
            return data;
        }).orElse(new HashMap<>());
    }
}
