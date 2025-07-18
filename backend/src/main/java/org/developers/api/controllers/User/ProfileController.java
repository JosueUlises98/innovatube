package org.developers.api.controllers.User;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetrics;
import org.developers.api.response.User.UserResponse;
import org.developers.api.response.User.UserSessionDetails;
import org.developers.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@Log4j2
public class ProfileController {

    private final UserServiceImpl userService;

    @Autowired
    public ProfileController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long userId) {
        log.info("Obteniendo perfil del usuario: {}", userId);
        try {
            UserSessionDetails userDetails = userService.getUserById(userId);
            // Convert UserSessionDetails to UserResponse
            UserResponse userResponse = UserResponse.builder()
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .build();
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.error("Error al obtener perfil del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserProfile request) {
        log.info("Actualizando perfil del usuario: {}", userId);
        request.setUserId(userId);
        try {
            UserResponse updatedUser = userService.updateUserProfile(request);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error al actualizar perfil del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long userId) {
        log.info("Eliminando perfil del usuario: {}", userId);
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar perfil del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/activity")
    public ResponseEntity<UserActivityMetrics> getUserActivityMetrics(@PathVariable Long userId) {
        log.info("Obteniendo métricas de actividad del usuario: {}", userId);
        try {
            UserActivityMetrics metrics = userService.getUserActivityMetricsById(userId);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            log.error("Error al obtener métricas de actividad del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/sessions")
    public ResponseEntity<List<UserSessionDetails>> getUserSessions(@PathVariable Long userId) {
        log.info("Obteniendo sesiones del usuario: {}", userId);
        try {
            List<UserSessionDetails> sessions = userService.getUserSessions(userId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            log.error("Error al obtener sesiones del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<UserResponse> updateUserAvatar(
            @PathVariable Long userId,
            @RequestBody UpdateUserAvatar request) {
        log.info("Actualizando avatar del usuario: {}", userId);
        request.setUserId(userId);
        try {
            UserResponse updatedUser = userService.updateUserAvatar(request);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error al actualizar avatar del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @RequestBody @Valid ChangePassword request) {
        log.info("Cambiando contraseña del usuario: {}", userId);
        request.setUserId(userId);
        try {
            userService.changePassword(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al cambiar contraseña del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/email")
    public ResponseEntity<UserResponse> updateEmail(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateEmail request) {
        log.info("Actualizando email del usuario: {}", userId);
        request.setUserId(userId);
        try {
            UserResponse updatedUser = userService.updateEmail(request);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error al actualizar email del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/username")
    public ResponseEntity<UserResponse> updateUsername(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUsername request) {
        log.info("Actualizando username del usuario: {}", userId);
        request.setUserId(userId);
        try {
            UserResponse updatedUser = userService.updateUsername(request);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error al actualizar username del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/preferences")
    public ResponseEntity<Map<String, Object>> getUserPreferences(@PathVariable Long userId) {
        log.info("Obteniendo preferencias del usuario: {}", userId);
        try {
            Map<String, Object> preferences = userService.getUserPreferences(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            log.error("Error al obtener preferencias del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userId}/preferences")
    public ResponseEntity<Void> updateUserPreferences(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> preferences) {
        log.info("Actualizando preferencias del usuario: {}", userId);
        try {
            userService.updateUserPreferences(userId, preferences);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al actualizar preferencias del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        log.info("Obteniendo estadísticas del usuario: {}", userId);
        try {
            Map<String, Object> statistics = userService.getUserStatistics(userId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        log.info("Desactivando usuario: {}", userId);
        try {
            userService.deactivateUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al desactivar usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        log.info("Activando usuario: {}", userId);
        try {
            userService.activateUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al activar usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/favorites/count")
    public ResponseEntity<Long> getUserFavoritesCount(@PathVariable Long userId) {
        log.info("Obteniendo conteo de favoritos del usuario: {}", userId);
        try {
            Long count = userService.getUserFavoritesCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error al obtener conteo de favoritos del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/videos/count")
    public ResponseEntity<Long> getUserVideosCount(@PathVariable Long userId) {
        log.info("Obteniendo conteo de videos del usuario: {}", userId);
        try {
            Long count = userService.getUserVideosCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error al obtener conteo de videos del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/recent-activity")
    public ResponseEntity<List<Map<String, Object>>> getUserRecentActivity(@PathVariable Long userId) {
        log.info("Obteniendo actividad reciente del usuario: {}", userId);
        try {
            List<Map<String, Object>> activity = userService.getUserRecentActivity(userId);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            log.error("Error al obtener actividad reciente del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/export-data")
    public ResponseEntity<Map<String, Object>> exportUserData(@PathVariable Long userId) {
        log.info("Exportando datos del usuario: {}", userId);
        try {
            Map<String, Object> exportData = userService.exportUserData(userId);
            return ResponseEntity.ok(exportData);
        } catch (Exception e) {
            log.error("Error al exportar datos del usuario: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 