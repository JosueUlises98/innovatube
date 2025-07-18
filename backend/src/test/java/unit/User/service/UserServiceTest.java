package unit.User.service;


import org.developers.api.request.User.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    /*
        @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private Logger logger;
    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private UserResponse mockUserResponse;
    private LocalDateTime now;
    private List<User> mockUserList;
    private List<UserResponse> mockUserResponseList;

    @BeforeEach
    void setUp() {
        logger = LoggerFactory.getLogger(UserServiceImpl.class);
        now = LocalDateTime.now();
        initDataMocks();
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
            LocalDateTime fechaReferencia = now.minusDays(5L);
            GetRecentlyActiveUsersRequest request = new GetRecentlyActiveUsersRequest();
            request.setSinceDate(fechaReferencia);

            // Configurar comportamiento de los mocks - AQUÍ ESTÁ EL CAMBIO PRINCIPAL
            when(userRepository.findByLastLoginAfter(request.getSinceDate())).thenReturn(mockUserList);
            when(userMapper.toUserResponse(mockUserList.get(0))).thenReturn(mockUserResponseList.get(0));
            when(userMapper.toUserResponse(mockUserList.get(1))).thenReturn(mockUserResponseList.get(1));
            when(userMapper.toUserResponse(mockUserList.get(2))).thenReturn(mockUserResponseList.get(2));

            // Ejecutar el método bajo prueba
            List<UserResponse> result = userService.getRecentlyActiveUsers(request);

            // Verificaciones
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(3);
            assertThat(result.get(0).getUsername()).isEqualTo("testUser");
            assertThat(result.get(1).getUsername()).isEqualTo("test1");

            // Verificar que los mocks fueron llamados correctamente
            verify(userRepository).findByLastLoginAfter(request.getSinceDate());
            verify(userMapper, times(3)).toUserResponse(any(User.class));
        }
    }

    @Nested
    @DisplayName("Tests de getUsersRegisteredInPeriod")
    class GetUsersRegisteredInPeriodTests {
        @Test
        @DisplayName("Debería retornar usuarios registrados en el período")
        void shouldReturnUsersRegisteredInPeriod() {
            GetUsersRegisteredInPeriodRequest request = new GetUsersRegisteredInPeriodRequest();
            request.setStartDate(now.minusDays(7));
            request.setEndDate(now);
            when(userRepository.findByCreatedAtBetween(request.getStartDate(), request.getEndDate())).thenReturn(mockUserList);
            when(userMapper.toUserResponse(mockUserList.get(0))).thenReturn(mockUserResponseList.get(0));
            when(userMapper.toUserResponse(mockUserList.get(1))).thenReturn(mockUserResponseList.get(1));
            when(userMapper.toUserResponse(mockUserList.get(2))).thenReturn(mockUserResponseList.get(2));

            List<UserResponse> result = userService.getUsersRegisteredInPeriod(request);

            assertThat(result.size()).isEqualTo(3);
            assertThat(result.getFirst().getUsername()).isEqualTo("testUser");
            assertThat(result.getLast().getUsername()).isEqualTo("test");
            verify(userRepository).findByCreatedAtBetween(request.getStartDate(), request.getEndDate());
            verify(userMapper, times(3)).toUserResponse(any(User.class));
        }
    }

    @Nested
    @DisplayName("Tests de getInactiveUsersSince")
    class GetInactiveUsersSinceTests {
        @Test
        @DisplayName("Debería retornar usuarios inactivos")
        void shouldReturnInactiveUsers() {
            GetInactiveUsersSinceRequest request = new GetInactiveUsersSinceRequest();
            request.setDate(now);

            when(userRepository.findInactiveUsers(request.getDate())).thenReturn(mockUserList);
            when(userMapper.toUserResponse(mockUserList.get(0))).thenReturn(mockUserResponseList.get(0));
            when(userMapper.toUserResponse(mockUserList.get(1))).thenReturn(mockUserResponseList.get(1));
            when(userMapper.toUserResponse(mockUserList.get(2))).thenReturn(mockUserResponseList.get(2));

            List<UserResponse> result = userService.getInactiveUsersSince(request);
            result.forEach(System.out::println);

            assertThat(result.size()).isEqualTo(3);
            assertThat(result.getFirst().getUsername()).isEqualTo("testUser");
            assertThat(result.getLast().getUsername()).isEqualTo("test");
            verify(userRepository).findInactiveUsers(request.getDate());
            verify(userMapper, times(3)).toUserResponse(any(User.class));
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
            when(userRepository.save(mockUser)).thenReturn(mockUser);

            //Nota:Se tiene que modificar el valor de lastlogin segun se requiera para este metodo de test
            userService.recordUserActivity(request);

            verify(userRepository).save(mockUser);
            assertThat(mockUser.getLastLogin()).isNotNull();
            assertThat(mockUser.getLastLogin().isBefore(now)).isTrue();
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
            when(userRepository.save(mockUser)).thenReturn(mockUser);

            //Nota:Se tiene que modificar el valor de lastlogin segun se requiera para este metodo de test
            userService.updateUserLastSession(request);

            verify(userRepository).save(mockUser);
            assertThat(mockUser.getLastLogin()).isNotNull();
            assertThat(mockUser.getLastLogin().isBefore(now)).isTrue();
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
            UserActivityMetricsResponse response = UserActivityMetricsResponse.builder()
                    .isActive(mockUser.getIsActive())
                    .lastLogin(mockUser.getLastLogin())
                    .build();
            when(userRepository.findUserMetrics(request.getUserId(),LocalDateTime.now().minusDays(2))).thenReturn(response);

            UserActivityMetricsResponse result = userService.getUserActivityMetrics(request);

            assertThat(result).isNotNull();
            assertThat(result.getIsActive()).isTrue();
            assertThat(result.getFavoriteCount()).isEqualTo(0);
            assertThat(result.getLastLogin()).isNotNull();
            verify(userRepository).findUserMetrics(request.getUserId(),LocalDateTime.now().minusDays(2));
        }
    }

    @Nested
    @DisplayName("Tests de deactivateInactiveUsers")
    class DeactivateInactiveUsersTests {
        @Test
        @DisplayName("Debería desactivar usuarios inactivos")
        void shouldDeactivateInactiveUsers() {
            DeactivateInactiveUsersRequest request = new DeactivateInactiveUsersRequest();
            request.setThresholdDate(now);

            when(userRepository.findInactiveUsers(request.getThresholdDate())).thenReturn(mockUserList);
            when(userRepository.save(mockUserList.get(0))).thenReturn(mockUserList.get(0));
            when(userRepository.save(mockUserList.get(1))).thenReturn(mockUserList.get(1));
            when(userRepository.save(mockUserList.get(2))).thenReturn(mockUserList.get(2));

            userService.deactivateInactiveUsers(request);

            verify(userRepository).save(mockUserList.get(0));
            verify(userRepository).save(mockUserList.get(1));
            verify(userRepository).save(mockUserList.get(2));
            verify(userRepository, times(3)).save(any(User.class));
            verify(userRepository).findInactiveUsers(request.getThresholdDate());
            verify(userRepository, times(3)).save(any(User.class));
            assertThat(mockUserList.get(0).getIsActive()).isFalse();
            assertThat(mockUserList.get(1).getIsActive()).isFalse();
            assertThat(mockUserList.get(2).getIsActive()).isFalse();
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
                    .build();

            mockUser.setUsername(request.getUsername());
            mockUser.setPassword(request.getPassword());
            mockUser.setEmail(request.getEmail());

            mockUserResponse.setUsername(request.getUsername());
            mockUserResponse.setEmail(request.getEmail());
            mockUserResponse.setPassword(request.getPassword());

            when(userRepository.save(mockUser)).thenReturn(mockUser);
            when(userRepository.findById(request.getId())).thenReturn(Optional.of(mockUser));
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
                    .build();

            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));

            userService.deleteUser(request);

            verify(userRepository).delete(mockUser);
            verify(userRepository).findByEmail(request.getEmail());
        }
    }

    @Nested
    @DisplayName("Tests de createUser")
    class CreateUserTests {
        @Test
        @DisplayName("Debería crear usuario exitosamente")
        void shouldCreateUserSuccessfully() {
            // Preparar el request
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("newUser")
                    .name("new")
                    .lastname("user")
                    .password("password123")
                    .email("new@example.com")
                    .build();

            // Crear el usuario esperado que coincida con el request
            User expectedUser = User.builder()
                    .username("newUser")
                    .name("new")
                    .lastname("user")
                    .password("password123")
                    .email("new@example.com")
                    .createdAt(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .isActive(true)
                    .build();

            // Configurar los stubs con argumentMatchers
            when(userMapper.toEntity(any(UserResponse.class))).thenReturn(expectedUser);
            when(userRepository.save(any(User.class))).thenReturn(expectedUser);
            when(userMapper.toUserResponse(any(User.class))).thenReturn(mockUserResponse);

            // Ejecutar el método
            UserResponse result = userService.createUser(request);

            // Verificaciones
            assertThat(result).isNotNull();
            verify(userRepository).save(any(User.class));
            verify(userMapper).toUserResponse(any(User.class));
            assertThat(result.getUsername()).isEqualTo("newUser");
            assertThat(result.getEmail()).isEqualTo("new@example.com");
        }
    }

    @Test
    void loginTest(){
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");
        User mockUser = User.builder().username("username").password("password").build();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(mockUser));
        when(bCryptPasswordEncoder.matches(request.getPassword(), mockUser.getPassword())).thenReturn(true);

        UserSessionDetailsResponse userSessionDetailsResponse = userService.login(request);

        assertThat(userSessionDetailsResponse.getUsername()).isEqualTo(request.getUsername());
        verify(userRepository).findByUsername(request.getUsername());
    }

    private void initDataMocks(){
        mockUser = User.builder()
                .userid(1L)
                .username("newUser")
                .name("new")
                .lastname("user")
                .password("sys")
                .email("new@example.com")
                .password("password123")
                .build();

        mockUserResponse = UserResponse.builder()
                .username(mockUser.getUsername())
                .email(mockUser.getEmail())
                .isActive(mockUser.getIsActive())
                .lastLogin(mockUser.getLastLogin())
                .createdAt(mockUser.getCreatedAt())
                .name(mockUser.getName())
                .lastname(mockUser.getLastname())
                .password(mockUser.getPassword())
                .build();

        // Crear usuarios de prueba
        User testUser = User.builder()
                .name("prueba")
                .username("test")
                .lastname("unit")
                .password("junit_10")
                .email("mockito@example.com")
                .createdAt(LocalDateTime.now().minusDays(5L))
                .lastLogin(now)
                .isActive(false)
                .build();

        User testUser1 = User.builder()
                .name("prueba1")
                .username("test1")
                .lastname("unit1")
                .password("junit_101")
                .email("mockito1@example.com")
                .createdAt(LocalDateTime.now().minusDays(5L))
                .lastLogin(now)
                .isActive(false)
                .build();

        UserResponse userResponseTestUser = UserResponse.builder()
                .name(testUser.getName())
                .lastname(testUser.getLastname())
                .username(testUser.getUsername())
                .password(testUser.getPassword())
                .email(testUser.getEmail())
                .createdAt(testUser.getCreatedAt())
                .lastLogin(testUser.getLastLogin())
                .isActive(false)
                .build();

        UserResponse userResponseTestUser1 = UserResponse.builder()
                .name(testUser1.getName())
                .lastname(testUser1.getLastname())
                .username(testUser1.getUsername())
                .password(testUser1.getPassword())
                .email(testUser1.getEmail())
                .createdAt(testUser1.getCreatedAt())
                .lastLogin(testUser1.getLastLogin())
                .isActive(false)
                .build();
        mockUserList = List.of(mockUser, testUser, testUser1);
        mockUserResponseList = List.of(mockUserResponse, userResponseTestUser1, userResponseTestUser);
    }
     */

}
