package org.developers.model.mapper;

import org.developers.model.DTO.UserDTO;
import org.developers.model.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "favorites", ignore = true)  // Para evitar ciclos infinitos
    UserDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User entity, UserDTO dto);

    @InheritInverseConfiguration
    User toEntity(UserDTO dto);
}
