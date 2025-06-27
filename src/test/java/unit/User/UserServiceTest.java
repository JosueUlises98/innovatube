package unit.User;


import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetricsResponse;
import org.developers.api.response.User.UserResponse;
import org.developers.model.entities.User;
import org.developers.model.mapper.UserMapper;
import org.developers.repository.UserRepository;
import org.developers.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Logger logger;
    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private UserResponse mockUserResponse;
    private LocalDateTime now;
    private List<User> mockUserList;

    @BeforeEach
    void setUp() {
        logger = LoggerFactory.getLogger(UserServiceImpl.class);
        now = LocalDateTime.now();
        mockUser = User.builder()
                .userid(1L)
                .username("testUser")
                .email("test@example.com")
                .isActive(true)
                .lastLogin(now)
                .createdAt(now)
                .name("test")
                .lastname("innovatube")
                .password("zorras_01")
                .build();

        mockUserResponse = UserResponse.builder()
                .username("testUser")
                .email("test@example.com")
                .isActive(true)
                .lastLogin(now)
                .createdAt(now)
                .name("test")
                .lastname("innovatube")
                .password("zorras_01")
                .build();
        mockUserList = Collections.singletonList(mockUser);
    }

    @Nested
    @DisplayName("Tests de getUserByCredentials")
    class GetUserByCredentialsTests {
        @Test
        @DisplayName("Debería retornar usuario cuando las credenciales son correctas")
        void shouldReturnUserWhenCredentialsAreValid() {
            GetUserByUserNameRequest request = new GetUserByUserNameRequest();
            request.setUsername("testUser");
            when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(mockUser));
            when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);

            Optional<UserResponse> result = userService.getUserByCredentials(request);

            assertThat(result).isPresent();
            assertThat(result.get().getUsername()).isEqualTo("testUser");
        }
    }

    @Nested
    @DisplayName("Tests de retrieveUserByEmail")
    class RetrieveUserByEmailTests {
        @Test
        @DisplayName("Debería retornar usuario cuando el email existe")
        void shouldReturnUserWhenEmailExists() {
            RetrieveUserByEmailRequest request = new RetrieveUserByEmailRequest();
            request.setEmail("test@example.com");
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
            when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);

            Optional<UserResponse> result = userService.retrieveUserByEmail(request);

            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        }
    }

    @Nested
    @DisplayName("Tests de isUsernameAvailable")
    class IsUsernameAvailableTests {
        @Test
        @DisplayName("Debería retornar true cuando el username está disponible")
        void shouldReturnTrueWhenUsernameIsAvailable() {
            IsUserNameAvaibleRequest request = new IsUserNameAvaibleRequest();
            request.setUsername("testUser");

            boolean result = userService.isUsernameAvailable(request);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("Tests de isEmailAvailable")
    class IsEmailAvailableTests {
        @Test
        @DisplayName("Debería retornar true cuando el email está disponible")
        void shouldReturnTrueWhenEmailIsAvailable() {
            IsEmailAvaibleRequest request = new IsEmailAvaibleRequest();
            request.setEmail("test@example.com");

            boolean result = userService.isEmailAvailable(request);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("Tests de isUserNameAvailableById")
    class IsUserNameAvailableByIdTests {
        @Test
        @DisplayName("Debería retornar true cuando el username está disponible para el ID")
        void shouldReturnTrueWhenUsernameIsAvailableForId() {
            IsUserNameAvaibleRequest request = new IsUserNameAvaibleRequest();
            request.setUsername("testUser");
            request.setUserId(1L);
            when(userRepository.existsById(request.getUserId())).thenReturn(true);

            boolean result = userService.isUserNameAvailableById(request);

            assertThat(result).isTrue();
            verify(userRepository).existsById(request.getUserId());
        }
    }

    @Nested
    @DisplayName("Tests de getRecentlyActiveUsers")
    class GetRecentlyActiveUsersTests {
        @Test
        @DisplayName("Debería retornar usuarios activos recientes")
        void shouldReturnRecentlyActiveUsers() {
            // Configurar la fecha de solicitud
            GetRecentlyActiveUsersRequest request = new GetRecentlyActiveUsersRequest();
            request.setSinceDate(now.minusDays(1L));

            // Crear usuarios de prueba con fechas de actividad
            User testUser = User.builder()
                    .name("prueba")
                    .username("test")
                    .lastname("unit")
                    .password("junit_10")
                    .email("mockito@example.com")
                    .build();

            User testUser1 = User.builder()
                    .name("prueba1")
                    .username("test1")
                    .lastname("unit1")
                    .password("junit_101")
                    .email("mockito1@example.com")
                    .build();

            List<User> activeUsers = List.of(testUser, testUser1);

            // Configurar comportamiento de los mocks
            when(userRepository.saveAll(activeUsers)).thenReturn(activeUsers);
            when(userRepository.findMostActiveUsers()).thenReturn(activeUsers);

            // Ejecutar el método bajo prueba
            userService.saveUsersInBatch(activeUsers);
            List<UserResponse> result = userService.getRecentlyActiveUsers(request);

            // Verificaciones
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(2);
            assertThat(result.get(0).getUsername()).isEqualTo("test");
            assertThat(result.get(1).getUsername()).isEqualTo("test1");

            // Verificar que los mocks fueron llamados
            verify(userRepository).saveAll(activeUsers);
            verify(userRepository).findMostActiveUsers();
        }
    }

    @Nested
    @DisplayName("Tests de getUsersRegisteredInPeriod")
    class GetUsersRegisteredInPeriodTests {
        @Test
        @DisplayName("Debería retornar usuarios registrados en el período")
        void shouldReturnUsersRegisteredInPeriod() {
            GetUsersRegisteredInPeriodRequest request = new GetUsersRegisteredInPeriodRequest();
            request.setStartDate(now.minusDays(1));
            request.setEndDate(now);
            List<User> registeredUsers = Collections.singletonList(mockUser);
            when(userRepository.findByCreatedAtBetween(request.getStartDate(), request.getEndDate())).thenReturn(registeredUsers);
            when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);

            List<UserResponse> result = userService.getUsersRegisteredInPeriod(request);

            assertThat(result.size()).isEqualTo(1);
            assertThat(result.getFirst().getUsername()).isEqualTo("testUser");
        }
    }

    @Nested
    @DisplayName("Tests de getInactiveUsersSince")
    class GetInactiveUsersSinceTests {
        @Test
        @DisplayName("Debería retornar usuarios inactivos")
        void shouldReturnInactiveUsers() {
            GetInactiveUsersSinceRequest request = new GetInactiveUsersSinceRequest();
            request.setDate(now.minusDays(1));
            List<User> inactiveUsers = Collections.singletonList(mockUser);
            when(userRepository.findInactiveUsers(request.getDate())).thenReturn(inactiveUsers);
            when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);

            List<UserResponse> result = userService.getInactiveUsersSince(request);

            assertThat(result.size()).isEqualTo(1);
            assertThat(result.getFirst().getUsername()).isEqualTo("testUser");
        }
    }

    @Nested
    @DisplayName("Tests de recordUserActivity")
    class RecordUserActivityTests {
        @Test
        @DisplayName("Debería registrar actividad del usuario")
        void shouldRecordUserActivity() {
            RecordUserActivityRequest request = new RecordUserActivityRequest();
            request.setUserId(1L);
            when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));

            userService.recordUserActivity(request);

            verify(userRepository).save(mockUser);
        }
    }

    @Nested
    @DisplayName("Tests de updateUserLastSession")
    class UpdateUserLastSessionTests {
        @Test
        @DisplayName("Debería actualizar última sesión del usuario")
        void shouldUpdateUserLastSession() {
            UpdateUserLastSessionRequest request = new UpdateUserLastSessionRequest();
            request.setUserId(1L);
            when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));

            userService.updateUserLastSession(request);

            verify(userRepository).save(mockUser);
        }
    }

    @Nested
    @DisplayName("Tests de getUserActivityMetrics")
    class GetUserActivityMetricsTests {
        @Test
        @DisplayName("Debería retornar métricas de actividad del usuario")
        void shouldReturnUserActivityMetrics() {
            GetUserActivityMetricsRequest request = new GetUserActivityMetricsRequest();
            request.setUserId(1L);
            when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));

            UserActivityMetricsResponse result = userService.getUserActivityMetrics(request);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Tests de deactivateInactiveUsers")
    class DeactivateInactiveUsersTests {
        @Test
        @DisplayName("Debería desactivar usuarios inactivos")
        void shouldDeactivateInactiveUsers() {
            DeactivateInactiveUsersRequest request = new DeactivateInactiveUsersRequest();
            request.setThresholdDate(now.minusDays(1));
            List<User> inactiveUsers = Collections.singletonList(mockUser);
            when(userRepository.findInactiveUsers(request.getThresholdDate())).thenReturn(inactiveUsers);

            userService.deactivateInactiveUsers(request);

            verify(userRepository, times(inactiveUsers.size())).save(mockUser);
        }
    }

    @Nested
    @DisplayName("Tests de updateUser")
    class UpdateUserTests {
        @Test
        @DisplayName("Debería actualizar usuario exitosamente")
        void shouldUpdateUserSuccessfully() {
            UpdateUserRequest request = UpdateUserRequest.builder()
                    .id(1L)
                    .username("updatedUser")
                    .password("<PASSWORD>")
                    .email("updated@example.com")
                    .createdAt(LocalDateTime.now().plusDays(1))
                    .lastLogin(LocalDateTime.now().plusDays(1))
                    .isActive(true)
                    .build();

            mockUser.setUsername("updatedUser");
            mockUser.setPassword("<PASSWORD>");
            mockUser.setEmail("updated@example.com");
            mockUser.setCreatedAt(LocalDateTime.now().plusDays(1));
            mockUser.setLastLogin(LocalDateTime.now().plusDays(1));

            mockUserResponse.setUsername("updatedUser");
            mockUserResponse.setEmail("updated@example.com");
            mockUserResponse.setPassword("<PASSWORD>");
            mockUserResponse.setCreatedAt(LocalDateTime.now().plusDays(1));
            mockUserResponse.setLastLogin(LocalDateTime.now().plusDays(1));

            when(userRepository.findById(request.getId())).thenReturn(Optional.of(mockUser));
            when(userRepository.save(mockUser)).thenReturn(mockUser);
            when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);

            UserResponse result = userService.updateUser(request);

            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("updatedUser");
            assertThat(result.getEmail()).isEqualTo("updated@example.com");
            assertThat(result.getPassword()).isEqualTo("<PASSWORD>");

        }
    }

    @Nested
    @DisplayName("Tests de deleteUser")
    class DeleteUserTests {
        @Test
        @DisplayName("Debería eliminar usuario exitosamente")
        void shouldDeleteUserSuccessfully() {
            DeleteUserRequest request = DeleteUserRequest.builder()
                    .email("updated@example.com")
                    .username("updatedUser")
                    .build();

            when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

            userService.deleteUser(request);

            verify(userRepository).delete(mockUser);
        }
    }

    @Nested
    @DisplayName("Tests de createUser")
    class CreateUserTests {
        @Test
        @DisplayName("Debería crear usuario exitosamente")
        void shouldCreateUserSuccessfully() {

            CreateUserRequest request = CreateUserRequest.builder()
                    .username("newUser")
                    .name("new")
                    .lastname("user")
                    .password("sys")
                    .email("new@example.com")
                    .password("password123")
                    .build();

            User user = User.builder()
                    .username("newUser")
                    .name("new")
                    .lastname("user")
                    .password("sys")
                    .email("new@example.com")
                    .password("password123")
                    .build();

            when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

            UserResponse result = userService.createUser(request);

            assertThat(result).isNotNull();
            verify(userRepository).save(user);
            assertThat(result.getUsername()).isEqualTo("newUser");
            assertThat(result.getEmail()).isEqualTo("new@example.com");
            assertThat(result.getPassword()).isEqualTo("password123");
        }
    }
}
