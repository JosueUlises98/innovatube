package org.developers.model.mapper;

import org.developers.api.response.Video.VideoResponse;
import org.developers.model.entities.Video;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    @Mapping(target = "favorites", ignore = true)  // Para evitar ciclos infinitos
    VideoResponse toVideoResponse(Video video);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Video entity, VideoResponse videoResponse);

    @InheritInverseConfiguration
    Video toEntity(VideoResponse videoResponse);
}
