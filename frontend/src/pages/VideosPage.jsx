import React, { useEffect, useState } from 'react';
import { videoAPI } from '../lib/api';
import VideoCard from '../components/VideoCard';
import { useAuth } from '../contexts/AuthContext';

const VideosPage = () => {
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();

  useEffect(() => {
    const fetchVideos = async () => {
      setLoading(true);
      setError(null);
      
      try {
        let data;
        if (user && user.id) {
          data = await videoAPI.getTrendingVideos(10, user.id);
        } else {
          // For unauthenticated users, try to get YouTube trending videos
          data = await videoAPI.getTrendingVideos(10);
        }
        
        // Validar que la respuesta sea un array
        setVideos(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error('Error fetching videos:', err);
        setError('Error al cargar los videos. Por favor, intenta de nuevo.');
        setVideos([]);
      } finally {
        setLoading(false);
      }
    };
    
    fetchVideos();
  }, [user]);

  if (loading) {
    return (
      <div className="min-h-screen bg-youtube-dark py-8 px-4 overflow-y-auto">
        <div className="flex items-center justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-youtube-red"></div>
          <span className="ml-4 text-youtube-text">Cargando videos...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-youtube-dark py-8 px-4 overflow-y-auto">
        <div className="max-w-4xl mx-auto text-center">
          <div className="bg-youtube-card rounded-lg p-8">
            <h2 className="text-2xl font-bold text-youtube-text mb-4">Error al cargar videos</h2>
            <p className="text-youtube-text-secondary mb-6">{error}</p>
            <button 
              onClick={() => window.location.reload()}
              className="bg-youtube-red hover:bg-red-700 text-white px-6 py-2 rounded-lg transition-colors"
            >
              Intentar de nuevo
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-youtube-dark py-8 px-4 overflow-y-auto">
      {videos.length === 0 ? (
        <div className="max-w-4xl mx-auto text-center">
          <div className="bg-youtube-card rounded-lg p-8">
            <h2 className="text-2xl font-bold text-youtube-text mb-4">No hay videos disponibles</h2>
            <p className="text-youtube-text-secondary mb-6">
              {user ? 
                'No se encontraron videos en tendencia. Intenta agregar algunos videos a tu biblioteca.' :
                'Inicia sesión para ver videos personalizados o explora videos de YouTube.'
              }
            </p>
            {!user && (
              <div className="flex justify-center space-x-4">
                <a
                  href="/login"
                  className="bg-youtube-red hover:bg-red-700 text-white px-6 py-2 rounded-lg transition-colors"
                >
                  Iniciar Sesión
                </a>
                <a
                  href="/register"
                  className="bg-youtube-card hover:bg-youtube-border text-youtube-text px-6 py-2 rounded-lg border border-youtube-border transition-colors"
                >
                  Registrarse
                </a>
              </div>
            )}
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {videos.map((video) => (
            <VideoCard key={video.id || video.youtubeVideoId} video={video} />
          ))}
        </div>
      )}
    </div>
  );
};

export default VideosPage; 