package org.developers.model.mapper;

import org.developers.model.DTO.VideoDTO;
import org.developers.model.entities.Video;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    @Mapping(target = "favorites", ignore = true)  // Para evitar ciclos infinitos
    VideoDTO toDto(Video video);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Video entity, VideoDTO dto);

    @InheritInverseConfiguration
    Video toEntity(VideoDTO dto);
}
