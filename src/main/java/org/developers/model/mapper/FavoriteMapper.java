package org.developers.model.mapper;

import org.developers.model.DTO.FavoriteDTO;
import org.developers.model.entities.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, VideoMapper.class})
public interface FavoriteMapper {

    @Mapping(target = "userId", source = "user")
    @Mapping(target = "videoId", source = "video")
    Favorite toEntity(FavoriteDTO dto);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "video", source = "videoId")
    FavoriteDTO toDto(Favorite favorite);

}
