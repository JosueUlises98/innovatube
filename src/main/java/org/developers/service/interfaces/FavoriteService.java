package org.developers.service.interfaces;

import org.developers.api.request.Favorite.*;
import org.developers.api.response.Favorite.FavoriteVideoResponse;
import org.developers.api.response.Favorite.PaginatedVideoFavoritesResponse;
import java.util.List;


public interface FavoriteService {

    // Gestión de favoritos del usuario
    List<FavoriteVideoResponse> getUserFavoriteVideos(GetUserFavoriteVideosRequest getUserFavoriteVideosRequest);

    // Verificación de estado
    boolean hasUserFavoritedVideo(HasUserFavoritedVideo hasUserFavoritedVideo);

    // Gestión de favoritos paginados
    PaginatedVideoFavoritesResponse getUserFavoritesPaginated(GetUserFavoritesPaginated getUserFavoritesPaginatedRequest);

    // Operaciones de gestión
    void addVideoToFavorites(AddVideoToFavorites addVideoToFavoritesRequest);
    void removeVideoFromFavorites(RemoveVideoFromFavorites removeVideoFromFavoritesRequest);

    List<FavoriteVideoResponse> getRecentlyFavoritedVideos(GetRecentlyFavoritedVideos request);
    List<FavoriteVideoResponse> getTrendingFavoriteVideos(GetTrendingFavoriteVideos request);
}
