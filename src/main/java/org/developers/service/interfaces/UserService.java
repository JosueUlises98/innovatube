package org.developers.service.interfaces;

import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetricsResponse;
import org.developers.api.response.User.UserResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    //Metodos fundamentales
    UserResponse createUser(CreateUserRequest createUserRequest);
    UserResponse updateUser(UpdateUserRequest updateUserRequest);
    void deleteUser(DeleteUserRequest deleteUserRequest);

    // Métodos de búsqueda y verificación de identidad
    Optional<UserResponse> getUserByCredentials(GetUserByUserNameRequest userNameRequest);
    Optional<UserResponse> retrieveUserByEmail(RetrieveUserByEmailRequest retrieveUserByEmailRequest);
    boolean isUsernameAvailable(IsUserNameAvaibleRequest userNameAvaibleRequest);
    boolean isEmailAvailable(IsEmailAvaibleRequest emailAvaibleRequest);
    boolean isUserNameAvailableById(IsUserNameAvaibleRequest userNameAvaibleRequest);

    // Métodos relacionados con la actividad del usuario
    List<UserResponse> getRecentlyActiveUsers(GetRecentlyActiveUsersRequest recentlyActiveUsersRequest);
    List<UserResponse> getUsersRegisteredInPeriod(GetUsersRegisteredInPeriodRequest usersRegisteredInPeriodRequest);

    // Métodos de análisis de actividad
    List<UserResponse> getInactiveUsersSince(GetInactiveUsersSinceRequest inactiveUsersSinceRequest);
    List<UserResponse> getMostEngagedUsers();

    // Métodos de gestión de sesión
    void recordUserActivity(RecordUserActivityRequest recordUserActivityRequest);
    void updateUserLastSession(UpdateUserLastSessionRequest updateUserLastSessionRequest);

    // Métodos adicionales que agregan valor de negocio
    UserActivityMetricsResponse getUserActivityMetrics(GetUserActivityMetricsRequest getUserActivityMetricsRequest);
    void deactivateInactiveUsers(DeactivateInactiveUsersRequest deactivateInactiveUsersRequest);
    boolean isUserActive(IsUserActiveRequest isUserActiveRequest);


}
