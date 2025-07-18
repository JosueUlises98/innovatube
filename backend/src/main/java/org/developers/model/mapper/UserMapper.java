package org.developers.model.mapper;

import org.developers.api.response.User.UserResponse;
import org.developers.api.response.User.UserSessionDetails;
import org.developers.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    UserResponse toUserResponse(User user);

    void updateEntity(@MappingTarget User entity, UserResponse userResponse);

    User toEntity(UserResponse userResponse);

    UserSessionDetails toUserSessionDetails(User user);
}
