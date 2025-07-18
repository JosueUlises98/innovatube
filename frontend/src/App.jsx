import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { Toaster } from 'react-hot-toast';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import VideosPage from './pages/VideosPage';
import VideoDetail from './pages/VideoDetail';
import FavoritesPage from './pages/FavoritesPage';
import RecoverPasswordPage from './pages/RecoverPasswordPage';
import ProfilePage from './pages/ProfilePage';
import { useAuth } from './contexts/AuthContext';
import VideoCard from './components/VideoCard';
import { videoAPI } from './lib/api';

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen bg-youtube-gray-darker flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-youtube-red"></div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

// Public Route Component (redirect if authenticated)
const PublicRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen bg-youtube-gray-darker flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-youtube-red"></div>
      </div>
    );
  }

  if (isAuthenticated) {
    return <Navigate to="/videos" replace />;
  }

  return children;
};

// Home Page Component that shows YouTube trending videos
const HomePage = () => {
  const { isAuthenticated } = useAuth();
  
  if (isAuthenticated) {
    return <VideosPage />;
  } else {
    return <YouTubeHomePage />;
  }
};

// YouTube Home Page Component for unauthenticated users
const YouTubeHomePage = () => {
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTrendingVideos = async () => {
      setLoading(true);
      setError(null);
      
      try {
        const data = await videoAPI.getYouTubeTrendingVideos('US', null, 20);
        setVideos(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error('Error fetching trending videos:', err);
        setError('Error al cargar los videos. Por favor, intenta de nuevo.');
        setVideos([]);
      } finally {
        setLoading(false);
      }
    };
    
    fetchTrendingVideos();
  }, []);

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
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-youtube-text mb-8 text-center">
          Videos en Tendencia
        </h1>
        {videos.length === 0 ? (
          <div className="text-center">
            <p className="text-youtube-text-secondary">No hay videos disponibles en este momento.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {videos.map((video) => (
              <VideoCard key={video.id || video.youtubeVideoId} video={video} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

// Main App Component
const AppContent = () => {
  return (
    <div className="min-h-screen bg-youtube-gray-darker">
      <Header />
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={<PublicRoute><LoginForm /></PublicRoute>} />
        <Route path="/register" element={<PublicRoute><RegisterForm /></PublicRoute>} />
        <Route path="/recover" element={<RecoverPasswordPage />} />
        
        {/* Home Page - Shows different content based on authentication */}
        <Route path="/" element={<HomePage />} />
        
        {/* Protected Routes */}
        <Route path="/videos" element={<ProtectedRoute><VideosPage /></ProtectedRoute>} />
        <Route path="/video/:videoId" element={<ProtectedRoute><VideoDetail /></ProtectedRoute>} />
        <Route path="/favorites" element={<ProtectedRoute><FavoritesPage /></ProtectedRoute>} />
        <Route path="/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />
        <Route
          path="/trending"
          element={
            <ProtectedRoute>
              <VideosPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/search"
          element={
            <ProtectedRoute>
              <VideosPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/shorts"
          element={
            <ProtectedRoute>
              <VideosPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/categories"
          element={
            <ProtectedRoute>
              <VideosPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/history"
          element={
            <ProtectedRoute>
              <VideosPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/recent"
          element={
            <ProtectedRoute>
              <VideosPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/settings"
          element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/help"
          element={
            <ProtectedRoute>
              <div className="min-h-screen bg-youtube-darker pt-14">
                <div className="max-w-4xl mx-auto px-4 py-8">
                  <h1 className="text-3xl font-bold text-youtube-text mb-8">Centro de Ayuda</h1>
                  <div className="bg-youtube-card rounded-lg p-6">
                    <h2 className="text-xl font-semibold text-youtube-text mb-4">Preguntas Frecuentes</h2>
                    <div className="space-y-4">
                      <div className="border-b border-youtube-border pb-4">
                        <h3 className="text-youtube-text font-medium mb-2">¿Cómo puedo buscar videos?</h3>
                        <p className="text-youtube-text-secondary">Usa la barra de búsqueda en la parte superior de la página para encontrar videos por título, descripción o etiquetas.</p>
                      </div>
                      <div className="border-b border-youtube-border pb-4">
                        <h3 className="text-youtube-text font-medium mb-2">¿Cómo agrego videos a favoritos?</h3>
                        <p className="text-youtube-text-secondary">Haz clic en el ícono de corazón en cualquier video para agregarlo a tus favoritos.</p>
                      </div>
                      <div className="border-b border-youtube-border pb-4">
                        <h3 className="text-youtube-text font-medium mb-2">¿Cómo cambio mi contraseña?</h3>
                        <p className="text-youtube-text-secondary">Ve a tu perfil y selecciona la pestaña "Seguridad" para cambiar tu contraseña.</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </ProtectedRoute>
          }
        />
        <Route
          path="/feedback"
          element={
            <ProtectedRoute>
              <div className="min-h-screen bg-youtube-darker pt-14">
                <div className="max-w-2xl mx-auto px-4 py-8">
                  <h1 className="text-3xl font-bold text-youtube-text mb-8">Enviar Comentarios</h1>
                  <div className="bg-youtube-card rounded-lg p-6">
                    <form className="space-y-4">
                      <div>
                        <label className="block text-youtube-text font-medium mb-2">Tipo de comentario</label>
                        <select className="w-full bg-youtube-darker border border-youtube-border rounded-lg px-4 py-2 text-youtube-text focus:outline-none focus:border-youtube-red">
                          <option>Bug report</option>
                          <option>Feature request</option>
                          <option>General feedback</option>
                        </select>
                      </div>
                      <div>
                        <label className="block text-youtube-text font-medium mb-2">Descripción</label>
                        <textarea 
                          className="w-full bg-youtube-darker border border-youtube-border rounded-lg px-4 py-2 text-youtube-text focus:outline-none focus:border-youtube-red h-32"
                          placeholder="Describe tu comentario..."
                        ></textarea>
                      </div>
                      <button className="w-full bg-youtube-accent hover:bg-red-700 text-white py-2 px-4 rounded-lg transition-colors">
                        Enviar Comentario
                      </button>
                    </form>
                  </div>
                </div>
              </div>
            </ProtectedRoute>
          }
        />
        <Route
          path="/keyboard-shortcuts"
          element={
            <ProtectedRoute>
              <div className="min-h-screen bg-youtube-darker pt-14">
                <div className="max-w-4xl mx-auto px-4 py-8">
                  <h1 className="text-3xl font-bold text-youtube-text mb-8">Atajos de Teclado</h1>
                  <div className="bg-youtube-card rounded-lg p-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <div>
                        <h2 className="text-xl font-semibold text-youtube-text mb-4">Navegación</h2>
                        <div className="space-y-2">
                          <div className="flex justify-between">
                            <span className="text-youtube-text-secondary">Ctrl + K</span>
                            <span className="text-youtube-text">Buscar</span>
                          </div>
                          <div className="flex justify-between">
                            <span className="text-youtube-text-secondary">F11</span>
                            <span className="text-youtube-text">Pantalla completa</span>
                          </div>
                        </div>
                      </div>
                      <div>
                        <h2 className="text-xl font-semibold text-youtube-text mb-4">Reproducción</h2>
                        <div className="space-y-2">
                          <div className="flex justify-between">
                            <span className="text-youtube-text-secondary">Espacio</span>
                            <span className="text-youtube-text">Play/Pause</span>
                          </div>
                          <div className="flex justify-between">
                            <span className="text-youtube-text-secondary">M</span>
                            <span className="text-youtube-text">Silenciar</span>
                          </div>
                          <div className="flex justify-between">
                            <span className="text-youtube-text-secondary">← →</span>
                            <span className="text-youtube-text">Retroceder/Avanzar 10s</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </ProtectedRoute>
          }
        />

        {/* Default Routes */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      
      {/* Toast Notifications */}
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            background: '#282828',
            color: '#ffffff',
            border: '1px solid #404040',
          },
          success: {
            iconTheme: {
              primary: '#10B981',
              secondary: '#ffffff',
            },
          },
          error: {
            iconTheme: {
              primary: '#EF4444',
              secondary: '#ffffff',
            },
          },
        }}
      />
    </div>
  );
};

// Root App Component
const App = () => {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
};

export default App; 