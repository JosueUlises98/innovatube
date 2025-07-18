import React, { useEffect, useState } from 'react';
import { favoritesAPI } from '../lib/api';
import VideoCard from '../components/VideoCard';

const FavoritesPage = () => {
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFavorites = async () => {
      setLoading(true);
      const data = await favoritesAPI.getUserFavorites();
      setFavorites(data || []);
      setLoading(false);
    };
    fetchFavorites();
  }, []);

  return (
    <div className="min-h-screen bg-youtube-dark py-8 px-4">
      <h2 className="text-2xl font-bold text-white mb-6">Videos que me gustan</h2>
      {loading ? (
        <div className="text-youtube-textMuted">Cargando...</div>
      ) : favorites.length === 0 ? (
        <div className="text-youtube-textMuted">No tienes videos favoritos.</div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {favorites.map((video) => (
            <VideoCard key={video.id} video={video} />
          ))}
        </div>
      )}
    </div>
  );
};

export default FavoritesPage; 