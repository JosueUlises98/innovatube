package org.developers.service.impl;

import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetricsResponse;
import org.developers.api.response.User.UserResponse;
import org.developers.model.entities.User;
import org.developers.model.mapper.UserMapper;
import org.developers.repository.UserRepository;
import org.developers.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<UserResponse> getUserByCredentials(GetUserByUserNameRequest request) {
        logger.info("Buscando usuario por credenciales: {}", request.getUsername());
        return userRepository.findByUsername(request.getUsername())
                .map(userMapper::toUserResponse);
    }

    public Optional<UserResponse> retrieveUserByEmail(RetrieveUserByEmailRequest request) {
        logger.info("Buscando usuario por email: {}", request.getEmail());
        return userRepository.findByEmail(request.getEmail())
                .map(userMapper::toUserResponse);
    }

    public boolean isUsernameAvailable(IsUserNameAvaibleRequest request) {
        logger.debug("Verificando disponibilidad del username: {}", request.getUsername());
        return userRepository.existsByUsername(request.getUsername());
    }

    public boolean isEmailAvailable(IsEmailAvaibleRequest request) {
        logger.debug("Verificando disponibilidad del email: {}", request.getEmail());
        return userRepository.existsByEmail(request.getEmail());
    }

    public boolean isUserNameAvailableById(IsUserNameAvaibleRequest userNameAvaibleRequest) {
        logger.debug("Verificando disponibilidad del username por id: {}", userNameAvaibleRequest.getUserId());
        return userRepository.existsById(userNameAvaibleRequest.getUserId());
    }

    public List<UserResponse> getRecentlyActiveUsers(GetRecentlyActiveUsersRequest request) {
        logger.info("Obteniendo usuarios activos desde: {}", request.getSinceDate());
        return userRepository.findByLastLoginAfter(request.getSinceDate())
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersRegisteredInPeriod(GetUsersRegisteredInPeriodRequest request) {
        logger.info("Buscando usuarios registrados entre {} y {}",request.getStartDate(), request.getEndDate());
        return userRepository.findByCreatedAtBetween(request.getStartDate(),request.getEndDate())
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getInactiveUsersSince(GetInactiveUsersSinceRequest request) {
        logger.info("Buscando usuarios inactivos desde: {}", request.getDate());
        return userRepository.findInactiveUsers(request.getDate())
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getMostEngagedUsers() {
        logger.info("Obteniendo usuarios más activos");
        return userRepository.findMostActiveUsers()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public void recordUserActivity(RecordUserActivityRequest request) {
        logger.debug("Registrando actividad para usuario ID: {}", request.getUserId());
        userRepository.findById(request.getUserId()).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    public void updateUserLastSession(UpdateUserLastSessionRequest request) {
        logger.debug("Actualizando última sesión para usuario ID: {}", request.getUserId());
        userRepository.findById(request.getUserId()).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    public UserActivityMetricsResponse getUserActivityMetrics(GetUserActivityMetricsRequest request) {
        logger.info("Obteniendo métricas de actividad para usuario ID: {}", request.getUserId());
        return userRepository.findUserMetrics(request.getUserId());
    }

    public void deactivateInactiveUsers(DeactivateInactiveUsersRequest request) {
        logger.info("Desactivando usuarios inactivos desde: {}", request.getThresholdDate());
        List<User> inactiveUsers = userRepository.findInactiveUsers(request.getThresholdDate());
        inactiveUsers.forEach(user -> {
            user.setIsActive(false);
            userRepository.save(user);
        });
        logger.info("Se desactivaron {} usuarios", inactiveUsers.size());
    }

    public boolean isUserActive(IsUserActiveRequest request) {
        logger.debug("Verificando si el usuario ID: {} está activo", request.getUserId());
        return userRepository.findById(request.getUserId())
                .map(User::getIsActive)
                .orElse(false);
    }

    public UserResponse updateUser(UpdateUserRequest request) {
        logger.info("Actualizando usuario ID: {}",request.getId());
        return userRepository.findById(request.getId())
                .map(existingUser -> {
                    userMapper.updateEntity(existingUser,UserResponse.builder()
                            .name(existingUser.getName())
                            .lastname(existingUser.getLastname())
                            .username(existingUser.getUsername())
                            .password(existingUser.getPassword())
                            .email(existingUser.getEmail())
                            .createdAt(existingUser.getCreatedAt())
                            .lastLogin(existingUser.getLastLogin())
                            .isActive(existingUser.getIsActive())
                            .build());
                    User savedUser = userRepository.save(existingUser);
                    logger.info("Usuario actualizado: {}", savedUser);
                    return userMapper.toUserResponse(savedUser);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void deleteUser(DeleteUserRequest request) {
        AtomicReference<User> loguser = new AtomicReference<>();
        logger.info("Eliminando usuario con email: {}", request.getEmail());
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            user.setIsActive(false);
            userRepository.delete(user);
            loguser.set(user);
        });
        logger.info("Usuario eliminado: {}",loguser.get() != null ? loguser.get().getUsername() : "No encontrado" );
    }

    public UserResponse createUser(CreateUserRequest request) {
        logger.info("Creando nuevo usuario");
        UserResponse userResponse = UserResponse.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();
        User user = userMapper.toEntity(userResponse);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        logger.info("Usuario creado: {}", savedUser);
        return userMapper.toUserResponse(savedUser);
    }

    public List<UserResponse> saveUsersInBatch(List<User> users) {
        logger.info("Guardando lista de usuarios en lote");
        users.forEach(user -> {
            user.setCreatedAt(LocalDateTime.now().minusDays(5L));
            user.setLastLogin(LocalDateTime.now().minusDays(1L));
            user.setIsActive(true);
        });
        List<User> savedUsers = userRepository.saveAll(users);
        List<UserResponse> userResponses = savedUsers.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
        logger.info("Se han guardado {} usuarios en lote", savedUsers.size());
        return userResponses;
    }
}
