package org.developers.service.interfaces;

import org.developers.api.request.Favorite.*;
import org.developers.api.response.Favorite.FavoriteVideo;
import org.developers.api.response.Favorite.PaginatedVideoFavorites;

import java.util.List;

public interface FavoriteService {
    List<FavoriteVideo> getUserFavoriteVideos(GetUserFavoriteVideosRequest getUserFavoriteVideosRequest);

    boolean hasUserFavoritedVideo(HasUserFavoritedVideo hasUserFavoritedVideo);

    PaginatedVideoFavorites getUserFavoritesPaginated(GetUserFavoritesPaginated getUserFavoritesPaginatedRequest);

    void addVideoToFavorites(AddVideoToFavorites addVideoToFavoritesRequest);

    void removeVideoFromFavorites(RemoveVideoFromFavorites removeVideoFromFavoritesRequest);

    List<FavoriteVideo> getTrendingFavoriteVideos(GetTrendingFavoriteVideos request);
}
