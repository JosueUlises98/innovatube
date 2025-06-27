package org.developers.model.mapper;

import org.developers.api.request.User.UpdateUserRequest;
import org.developers.api.response.User.UserResponse;
import org.developers.model.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "favorites", ignore = true)  // Para evitar ciclos infinitos
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User entity, UserResponse userResponse);

    @InheritInverseConfiguration
    User toEntity(UserResponse userResponse);
}
