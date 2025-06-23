package org.developers.model.mapper;

import org.developers.api.request.Favorite.AddVideoToFavorites;
import org.developers.api.response.Favorite.FavoriteVideoResponse;
import org.developers.api.response.Favorite.FavoriteVideoStatistics;
import org.developers.api.response.Favorite.PaginatedVideoFavoritesResponse;
import org.developers.model.entities.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, VideoMapper.class})
public interface FavoriteMapper {

    @Mapping(target = "userId", source = "user")
    @Mapping(target = "videoId", source = "video")
    Favorite toEntity(FavoriteVideoResponse favoriteResponse);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "video", source = "videoId")
    FavoriteVideoResponse toFavoriteResponse(Favorite favorite);

    Favorite toEntity(AddVideoToFavorites request);
    List<FavoriteVideoResponse> toResponseList(List<Favorite> entities);
    FavoriteVideoResponse toResponse(Favorite entity);
    PaginatedVideoFavoritesResponse toPaginatedResponse(Page<Favorite> page);
    FavoriteVideoStatistics toStatisticsResponse(FavoriteVideoStatistics statistics);

}
