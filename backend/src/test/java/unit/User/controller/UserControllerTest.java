package unit.User.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.developers.api.controllers.User.UserController;
import org.developers.api.request.User.*;
import org.developers.api.response.User.UserResponse;
import org.developers.common.exception.exceptions.UserNotFoundException;
import org.developers.common.exception.handler.GlobalExceptionHandler;
import org.developers.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    /*
        private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {
        @Test
        void createUser_ConDatosValidos_DeberiaRetornarUsuarioCreado() throws Exception {
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("usuario_test")
                    .email("test@example.com")
                    .password("Password123!")
                    .name("Nombre")
                    .lastname("Apellido")
                    .build();

            UserResponse expectedResponse = UserResponse.builder()
                    .username("usuario_test")
                    .email("test@example.com")
                    .password("Password123!")
                    .name("Nombre")
                    .lastname("Apellido")
                    .build();

            when(userService.createUser(request)).thenReturn(expectedResponse);

            mockMvc.perform(post("/api/v1/users/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        }

        @Test
        void createUser_ConDatosInvalidos_DeberiaRetornarError400() throws Exception {
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("") // Invalid: Empty username
                    .email("invalid-email") // Invalid: Malformed email
                    .password("123") // Invalid: Password too short
                    .build();

            mockMvc.perform(post("/api/v1/users/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createUser_CuandoServiceLanzaExcepcion_DeberiaRetornarError500() throws Exception {
            // 1. Preparar los datos de prueba
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("usuario_test")
                    .email("test@example.com")
                    .password("Password123!")
                    .name("Nombre")
                    .lastname("Apellido")
                    .build();

            // 2. Configurar el comportamiento del mock de manera más específica
            try {
                doThrow(new RuntimeException("Error al crear usuario"))
                        .when(userService)
                        .createUser(request);
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }

            // 3. Ejecutar y verificar
            mockMvc.perform(post("/api/v1/users/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andDo(print()) // Esto nos ayudará a ver la respuesta completa en caso de error
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Get User By Credentials Tests")
    class GetUserByCredentialsTests {
        @Test
        void getUserByCredentials_ConCredencialesValidas_DeberiaRetornarUsuario() throws Exception {
            GetUserByUserNameRequest request = new GetUserByUserNameRequest();
            request.setUsername("usuario_test");

            UserResponse expectedResponse = UserResponse.builder()
                    .username("usuario_test")
                    .build();

            when(userService.getUserByCredentials(any(GetUserByUserNameRequest.class)))
                    .thenReturn(Optional.ofNullable(expectedResponse));

            mockMvc.perform(get("/api/v1/users/credentials/find")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        }

        @Test
        void getUserByCredentials_ConCredencialesInvalidas_DeberiaRetornarError404() throws Exception {
            GetUserByUserNameRequest request = new GetUserByUserNameRequest();
            request.setUsername("usuario_inexistente");

            when(userService.getUserByCredentials(request))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/users/credentials/find")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Get User By Email Tests")
    class GetUserByEmailTests {
        @Test
        void getUserByEmail_ConEmailValido_DeberiaRetornarUsuario() throws Exception {
            String email = "test@example.com";
            RetrieveUserByEmailRequest request = new RetrieveUserByEmailRequest();
            request.setEmail(email);
            UserResponse expectedResponse = UserResponse.builder()
                    .email(email)
                    .build();

            when(userService.retrieveUserByEmail(request)).thenReturn(Optional.ofNullable(expectedResponse));

            mockMvc.perform(get("/api/v1/users/email/find/{email}", email))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                    .andDo(print());
        }

        @Test
        void getUserByEmail_ConEmailInexistente_DeberiaRetornarError404() throws Exception {
            String email = "inexistente@example.com";
            RetrieveUserByEmailRequest request = new RetrieveUserByEmailRequest();
            request.setEmail(email);
            when(userService.retrieveUserByEmail(request)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/users/email/find/{email}", email))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class UsernameAvailabilityTests {
        @Test
        void isUsernameAvailable_ConUsernameDisponible_DeberiaRetornarTrue() throws Exception {
            IsUserNameAvaibleRequest request = new IsUserNameAvaibleRequest();
            request.setUserId(1L);
            request.setUsername("nuevo_usuario");

            when(userService.isUsernameAvailable(request))
                    .thenReturn(true);

            mockMvc.perform(get("/api/v1/users/availability/username")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andDo(print());
        }

        @Test
        void isUsernameAvailable_ConUsernameOcupado_DeberiaRetornarFalse() throws Exception {
            IsUserNameAvaibleRequest request = new IsUserNameAvaibleRequest();
            request.setUserId(1L);
            request.setUsername("usuario_existente");

            when(userService.isUsernameAvailable(request))
                    .thenReturn(false);

            mockMvc.perform(get("/api/v1/users/availability/username")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class EmailAvailabilityTests {
        @Test
        void isEmailAvailable_ConEmailDisponible_DeberiaRetornarTrue() throws Exception {
            IsEmailAvaibleRequest request = new IsEmailAvaibleRequest();
            request.setEmail("nuevo@example.com");

            when(userService.isEmailAvailable(request))
                    .thenReturn(true);

            mockMvc.perform(get("/api/v1/users/availability/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        @Test
        void isEmailAvailable_ConEmailOcupado_DeberiaRetornarFalse() throws Exception {
            IsEmailAvaibleRequest request = new IsEmailAvaibleRequest();
            request.setEmail("existente@example.com");

            when(userService.isEmailAvailable(request))
                    .thenReturn(false);

            mockMvc.perform(get("/api/v1/users/availability/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class RecentlyActiveUsersTests {
        @Test
        void getRecentlyActiveUsers_ConUsuariosActivos_DeberiaRetornarLista() throws Exception {
            GetRecentlyActiveUsersRequest request = new GetRecentlyActiveUsersRequest();
            request.setSinceDate(LocalDate.now().minusDays(10).atStartOfDay());

            List<UserResponse> expectedResponse = Arrays.asList(
                    createMockUserResponse("user1"),
                    createMockUserResponse("user2")
            );

            when(userService.getRecentlyActiveUsers(request))
                    .thenReturn(expectedResponse);

            mockMvc.perform(get("/api/v1/users/activity/recent")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                    .andDo(print());
        }

        @Test
        void getRecentlyActiveUsers_SinUsuariosActivos_DeberiaRetornarListaVacia() throws Exception {
            GetRecentlyActiveUsersRequest request = new GetRecentlyActiveUsersRequest();
            request.setSinceDate(LocalDate.now().minusDays(10).atStartOfDay());
            when(userService.getRecentlyActiveUsers(request))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/users/activity/recent")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class MostEngagedUsersTests {
        @Test
        void getMostEngagedUsers_ConUsuarios_DeberiaRetornarListaOrdenada() throws Exception {
            List<UserResponse> expectedResponse = Arrays.asList(
                    createMockUserResponse("user1"),
                    createMockUserResponse("user2")
            );

            when(userService.getMostEngagedUsers()).thenReturn(expectedResponse);

            mockMvc.perform(get("/api/v1/users/activity/most-engaged"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                    .andDo(print());
        }

        @Test
        void getMostEngagedUsers_SinUsuarios_DeberiaRetornarListaVacia() throws Exception {
            when(userService.getMostEngagedUsers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/users/activity/most-engaged"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class RecordUserActivityTests {
        @Test
        void recordUserActivity_ConDatosValidos_DeberiaRetornarExito() throws Exception {
            RecordUserActivityRequest request = new RecordUserActivityRequest();
            request.setUserId(1L);

            mockMvc.perform(post("/api/v1/users/activity/record")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(userService).recordUserActivity(any(RecordUserActivityRequest.class));
        }

        @Test
        void recordUserActivity_ConDatosInvalidos_DeberiaRetornarError400() throws Exception {
            RecordUserActivityRequest request = new RecordUserActivityRequest();
            request.setUserId(1L);

            doThrow(new IllegalArgumentException("Argumento ilegal")).when(userService).recordUserActivity(request);

            mockMvc.perform(post("/api/v1/users/activity/record")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class UserActivityMetricsTests {
        @Test
        void getUserActivityMetrics_ConUsuarioValido_DeberiaRetornarMetricas() throws Exception {
            Long userId = 1L;
            GetUserActivityMetricsRequest request = new GetUserActivityMetricsRequest();
            request.setUserId(userId);
            UserActivityMetricsResponse response = UserActivityMetricsResponse.builder()
                    .isActive(true)
                    .lastLogin(LocalDateTime.now())
                    .favoriteCount(2)
                    .build();

            when(userService.getUserActivityMetrics(request)).thenReturn(response);

            mockMvc.perform(get("/api/v1/users/metrics/get/{userId}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        void getUserActivityMetrics_ConUsuarioInexistente_DeberiaRetornarError404() throws Exception {
            // Arrange
            Long userId = 1L;
            GetUserActivityMetricsRequest request = new GetUserActivityMetricsRequest();
            request.setUserId(userId);

            // Simular que el servicio lanza una excepción de recurso no encontrado
            when(userService.getUserActivityMetrics(any(GetUserActivityMetricsRequest.class)))
                    .thenThrow(new UserNotFoundException("Usuario no encontrado con ID: " + userId));

            // Act & Assert
            mockMvc.perform(get("/api/v1/users/metrics/get/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class UpdateUserTests {
        @Test
        void updateUser_ConDatosValidos_DeberiaRetornarUsuarioActualizado() throws Exception {
            Long userId = 1L;
            UpdateUserRequest request = UpdateUserRequest.builder()
                    .id(userId)
                    .username("updated_user")
                    .password("RandomPass123!")
                    .email("random@example.com")
                    .build();

            UserResponse expectedResponse = UserResponse.builder()
                    .username("updated_user")
                    .password("RandomPass123!")
                    .email("random@example.com")
                    .createdAt(LocalDateTime.now().minusDays(10))
                    .lastLogin(LocalDateTime.now().minusDays(5))
                    .isActive(true)
                    .build();

            when(userService.updateUser(request))
                    .thenReturn(expectedResponse);

            mockMvc.perform(put("/api/v1/users/update/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                    .andDo(print());
            verify(userService).updateUser(any(UpdateUserRequest.class));
        }

        @Test
        void updateUser_ConDatosInvalidos_DeberiaRetornarError400() throws Exception {
            Long userId = 1L;
            UpdateUserRequest request = UpdateUserRequest.builder()
                    .id(userId)
                    .username("88888")
                    .password("invalid-password")
                    .email("random@example.com")
                    .build();
            when(userService.updateUser(request)).thenThrow(new IllegalArgumentException("Argumento ilegal"));

            mockMvc.perform(put("/api/v1/users/update/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Get User By Id Tests")
    class DeleteUserTests {
        @Test
        void deleteUser_ConUsuarioExistente_DeberiaRetornarExito() throws Exception {
            DeleteUserRequest request = DeleteUserRequest.builder()
                    .email("username@example.com")
                    .build();

            doNothing().when(userService).deleteUser(request);

            mockMvc.perform(delete("/api/v1/users/delete/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            verify(userService).deleteUser(any(DeleteUserRequest.class));
        }

        @Test
        void deleteUser_ConUsuarioInexistente_DeberiaRetornarError404() throws Exception {
            DeleteUserRequest request = DeleteUserRequest.builder()
                    .email("user1@email.com")
                    .build();

            doThrow(new UserNotFoundException("Usuario no encontrado con email: " + request.getEmail())).when(userService).deleteUser(request);

            mockMvc.perform(delete("/api/v1/users/delete/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Deactivate Inactive Users Tests")
    class DeactivateInactiveUsersTests {
        @Test
        void deactivateInactiveUsers_ConParametrosValidos_DeberiaRetornarExito() throws Exception {
            DeactivateInactiveUsersRequest request = new DeactivateInactiveUsersRequest();
            request.setThresholdDate(LocalDateTime.now().minusDays(10));

            doNothing().when(userService).deactivateInactiveUsers(request);

            mockMvc.perform(post("/api/v1/users/deactivate/inactive-users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(userService).deactivateInactiveUsers(any(DeactivateInactiveUsersRequest.class));
        }

        @Test
        void deactivateInactiveUsers_ConParametrosInvalidos_DeberiaRetornarError400() throws Exception {
            DeactivateInactiveUsersRequest request = new DeactivateInactiveUsersRequest();
            request.setThresholdDate(LocalDateTime.now());

            doThrow(new IllegalArgumentException("Argumento ilegal")).when(userService).deactivateInactiveUsers(request);

            mockMvc.perform(post("/api/v1/users/deactivate/inactive-users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    private UserResponse createMockUserResponse(String username) {
        return UserResponse.builder()
                .username(username)
                .build();
    }
     */

}
