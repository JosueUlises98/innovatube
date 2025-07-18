package org.developers.model.mapper;

import org.developers.api.response.Video.VideoResponse;
import org.developers.model.entities.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface VideoMapper {
    @Mapping(
            source = "likeCount",
            target = "likes"
    )
    Video toEntity(VideoResponse videoResponse);

    @Mapping(
            source = "likes",
            target = "likeCount"
    )
    VideoResponse toVideoResponse(Video video);

    @Mapping(
            source = "likeCount",
            target = "likes"
    )
    void updateEntity(@MappingTarget Video entity, VideoResponse videoResponse);
}
