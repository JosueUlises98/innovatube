package org.developers.service.interfaces;

import org.developers.api.request.User.*;
import org.developers.api.response.User.UserActivityMetrics;
import org.developers.api.response.User.UserResponse;
import org.developers.api.response.User.UserSessionDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    UserResponse createUser(CreateUser createUserRequest);

    UserResponse updateUser(UpdateUser updateUserRequest);

    void deleteUser(DeleteUser deleteUserRequest);

    UserSessionDetails login(Login loginRequest);

    UserSessionDetails getUserById(Long userId);

    Optional<UserResponse> getUserByCredentials(GetUserByUserName userNameRequest);

    Optional<UserResponse> retrieveUserByEmail(RetrieveUserByEmail retrieveUserByEmailRequest);

    boolean isUsernameAvailable(IsUserNameAvailable userNameAvaibleRequest);

    boolean isEmailAvailable(IsEmailAvailable emailAvaibleRequest);

    boolean isUserNameAvailableById(IsUserNameAvailable userNameAvaibleRequest);

    List<UserResponse> getRecentlyActiveUsers(GetRecentlyActiveUsers recentlyActiveUsersRequest);

    List<UserResponse> getUsersRegisteredInPeriod(GetUsersRegisteredInPeriod usersRegisteredInPeriodRequest);

    List<UserResponse> getInactiveUsersSince(GetInactiveUsersSince inactiveUsersSinceRequest);

    List<UserResponse> getMostEngagedUsers();

    void recordUserActivity(RecordUserActivity recordUserActivityRequest);

    void updateUserLastSession(UpdateUserLastSession updateUserLastSessionRequest);

    UserActivityMetrics getUserActivityMetrics(GetUserActivityMetrics getUserActivityMetricsRequest);

    void deactivateInactiveUsers(DeactivateInactiveUsers deactivateInactiveUsersRequest);

    // New methods for ProfileController
    UserResponse updateUserProfile(UpdateUserProfile updateUserProfileRequest);
    
    void deleteUserById(Long userId);
    
    UserActivityMetrics getUserActivityMetricsById(Long userId);
    
    List<UserSessionDetails> getUserSessions(Long userId);
    
    UserResponse updateUserAvatar(UpdateUserAvatar updateUserAvatarRequest);
    
    void changePassword(ChangePassword changePasswordRequest);
    
    UserResponse updateEmail(UpdateEmail updateEmailRequest);
    
    UserResponse updateUsername(UpdateUsername updateUsernameRequest);
    
    Map<String, Object> getUserPreferences(Long userId);
    
    void updateUserPreferences(Long userId, Map<String, Object> preferences);
    
    Map<String, Object> getUserStatistics(Long userId);
    
    void deactivateUser(Long userId);
    
    void activateUser(Long userId);
    
    Long getUserFavoritesCount(Long userId);
    
    Long getUserVideosCount(Long userId);
    
    List<Map<String, Object>> getUserRecentActivity(Long userId);
    
    Map<String, Object> exportUserData(Long userId);
}
