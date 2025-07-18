package org.developers.model.mapper;

import org.developers.model.dto.FavoriteVideo;
import org.developers.model.entities.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface FavoriteMapper {
    @Mappings({@Mapping(
            target = "title",
            source = "videoTitle"
    ), @Mapping(
            target = "description",
            source = "videoDescription"
    ), @Mapping(
            target = "duration",
            expression = "java(favorite.duration() != null ? favorite.duration().toString() : null)"
    ), @Mapping(
            target = "youtubeVideoId"
    ), @Mapping(
            target = "thumbnailUrl"
    ), @Mapping(
            target = "viewCount"
    ), @Mapping(
            target = "likes"
    ), @Mapping(
            target = "addedAt"
    )})
    org.developers.api.response.Favorite.FavoriteVideo toResponse(FavoriteVideo favorite);

    Favorite favoriteVideoToEntity(FavoriteVideo favorite);

    default List<org.developers.api.response.Favorite.FavoriteVideo> toResponseList(List<FavoriteVideo> favoriteVideos) {
        return favoriteVideos.stream().map(this::toResponse).toList();
    }
}
