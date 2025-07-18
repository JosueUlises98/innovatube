const API_BASE_URL = 'http://localhost:8080/api';

// Authentication endpoints
export const authAPI = {
  login: (credentials) => 
    fetch(`${API_BASE_URL}/v1/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials)
    }).then(res => res.json()),

  register: (userData) => 
    fetch(`${API_BASE_URL}/v1/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userData)
    }).then(res => res.json()),

  verifyRecaptcha: (token) => 
    fetch(`${API_BASE_URL}/v1/auth/verify-recaptcha`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ token })
    }).then(res => res.json())
};

// User endpoints
export const userAPI = {
  getProfile: (token) => 
    fetch(`${API_BASE_URL}/v1/users/profile`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  updateProfile: (token, userData) => 
    fetch(`${API_BASE_URL}/v1/users/profile`, {
      method: 'PUT',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(userData)
    }).then(res => res.json()),

  changePassword: (token, passwordData) => 
    fetch(`${API_BASE_URL}/v1/users/change-password`, {
      method: 'PUT',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(passwordData)
    }).then(res => res.json()),

  deleteAccount: (token) => 
    fetch(`${API_BASE_URL}/v1/users/delete`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  // --- Disponibilidad de username y email ---
  checkUsernameAvailability: async (username) => {
    const res = await fetch(`${API_BASE_URL}/v1/users/availability/username?username=${encodeURIComponent(username)}`);
    return await res.json(); // Devuelve true o false directamente
  },
  checkEmailAvailability: async (email) => {
    const res = await fetch(`${API_BASE_URL}/v1/users/availability/email?email=${encodeURIComponent(email)}`);
    return await res.json();
  },
};

// Video endpoints
export const videoAPI = {
  // Local video operations
  createVideo: (token, videoData) => 
    fetch(`${API_BASE_URL}/v1/videos/create`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(videoData)
    }).then(res => res.json()),

  searchVideos: (query, regionCode, languageCode) => 
    fetch(`${API_BASE_URL}/v1/videos/search?query=${encodeURIComponent(query)}&regionCode=${regionCode}&languageCode=${languageCode}`)
      .then(res => res.json()),

  getVideoByYoutubeId: (youtubeId, userId) => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/${youtubeId}?userId=${userId}`)
      .then(res => res.json()),

  searchVideosByTitle: (title, userId) => 
    fetch(`${API_BASE_URL}/v1/videos/search/title?title=${encodeURIComponent(title)}&userId=${userId}`)
      .then(res => res.json()),

  getLatestVideos: (page = 0, size = 10, userId) => 
    fetch(`${API_BASE_URL}/v1/videos/latest?page=${page}&size=${size}&userId=${userId}`)
      .then(res => res.json()),

  searchVideosWithPagination: (query, page = 0, size = 10, userId) => 
    fetch(`${API_BASE_URL}/v1/videos/search/paginated?query=${encodeURIComponent(query)}&page=${page}&size=${size}&userId=${userId}`)
      .then(res => res.json()),

  getTrendingVideos: (viewLimit = 10, userId) => {
    if (userId) {
      let url = `${API_BASE_URL}/v1/videos/trending?viewlimit=${viewLimit}&userId=${userId}`;
      return fetch(url).then(res => res.json());
    } else {
      // For unauthenticated users, use YouTube trending endpoint
      let url = `${API_BASE_URL}/v1/videos/youtube/trending?maxResults=${viewLimit}`;
      return fetch(url).then(res => res.json());
    }
  },

  getMostPopularVideos: (limit = 10) => 
    fetch(`${API_BASE_URL}/v1/videos/popular?limit=${limit}`)
      .then(res => res.json()),

  getVideosAddedInPeriod: (startDate, endDate, userId) => 
    fetch(`${API_BASE_URL}/v1/videos/period?startDate=${startDate}&endDate=${endDate}&userId=${userId}`)
      .then(res => res.json()),

  getShortFormVideos: (duration = "10") => 
    fetch(`${API_BASE_URL}/v1/videos/shorts?duration=${duration}`)
      .then(res => res.json()),

  getVideoStatistics: (videoId) => 
    fetch(`${API_BASE_URL}/v1/videos/${videoId}/statistics`)
      .then(res => res.json()),

  getVideosByDurationCategories: () => 
    fetch(`${API_BASE_URL}/v1/videos/duration-categories`)
      .then(res => res.json()),

  isVideoAvailableById: (videoId) => 
    fetch(`${API_BASE_URL}/v1/videos/available/id/${videoId}`)
      .then(res => res.json()),

  isVideoAvailableByTitle: (title) => 
    fetch(`${API_BASE_URL}/v1/videos/available/title?title=${encodeURIComponent(title)}`)
      .then(res => res.json()),

  // YouTube API integration
  searchYouTubeVideos: (query, maxResults = 20, regionCode = 'US') => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/search?query=${encodeURIComponent(query)}&maxResults=${maxResults}&regionCode=${regionCode}`)
      .then(res => res.json()),

  getYouTubeVideoDetails: (videoId) => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/videos/${videoId}`)
      .then(res => res.json()),

  getYouTubeTrendingVideos: (regionCode = 'US', categoryId = null, maxResults = 20) => {
    const params = new URLSearchParams({
      regionCode,
      maxResults: maxResults.toString()
    });
    if (categoryId) params.append('categoryId', categoryId);
    
    return fetch(`${API_BASE_URL}/v1/videos/youtube/trending?${params}`)
      .then(res => res.json());
  },

  getYouTubeCategories: (regionCode = 'US') => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/categories?regionCode=${regionCode}`)
      .then(res => res.json()),

  getYouTubeRelatedVideos: (videoId, maxResults = 20) => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/videos/${videoId}/related?maxResults=${maxResults}`)
      .then(res => res.json()),

  getYouTubeChannelDetails: (channelId) => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/channels/${channelId}`)
      .then(res => res.json()),

  getYouTubeVideoComments: (videoId, maxResults = 20) => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/videos/${videoId}/comments?maxResults=${maxResults}`)
      .then(res => res.json()),

  getYouTubePlaylistDetails: (playlistId) => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/playlists/${playlistId}`)
      .then(res => res.json()),

  getYouTubePlaylistVideos: (playlistId, maxResults = 20) => 
    fetch(`${API_BASE_URL}/v1/videos/youtube/playlists/${playlistId}/videos?maxResults=${maxResults}`)
      .then(res => res.json())
};

// Direct YouTube API endpoints (alternative)
export const youtubeAPI = {
  searchVideos: (query, maxResults = 20, regionCode = 'US') => 
    fetch(`${API_BASE_URL}/youtube/search?query=${encodeURIComponent(query)}&maxResults=${maxResults}&regionCode=${regionCode}`)
      .then(res => res.json()),

  getVideoDetails: (videoId) => 
    fetch(`${API_BASE_URL}/youtube/videos/${videoId}`)
      .then(res => res.json()),

  getVideoStatistics: (videoId) => 
    fetch(`${API_BASE_URL}/youtube/videos/${videoId}/statistics`)
      .then(res => res.json()),

  getChannelDetails: (channelId) => 
    fetch(`${API_BASE_URL}/youtube/channels/${channelId}`)
      .then(res => res.json()),

  getVideoComments: (videoId, maxResults = 20) => 
    fetch(`${API_BASE_URL}/youtube/videos/${videoId}/comments?maxResults=${maxResults}`)
      .then(res => res.json()),

  getTrendingVideos: (regionCode = 'US', categoryId = null, maxResults = 20) => {
    const params = new URLSearchParams({
      regionCode,
      maxResults: maxResults.toString()
    });
    if (categoryId) params.append('categoryId', categoryId);
    
    return fetch(`${API_BASE_URL}/youtube/trending?${params}`)
      .then(res => res.json());
  },

  getVideoCategories: (regionCode = 'US') => 
    fetch(`${API_BASE_URL}/youtube/categories?regionCode=${regionCode}`)
      .then(res => res.json()),

  getRelatedVideos: (videoId, maxResults = 20) => 
    fetch(`${API_BASE_URL}/youtube/videos/${videoId}/related?maxResults=${maxResults}`)
      .then(res => res.json()),

  getPlaylistDetails: (playlistId) => 
    fetch(`${API_BASE_URL}/youtube/playlists/${playlistId}`)
      .then(res => res.json()),

  getPlaylistVideos: (playlistId, maxResults = 20) => 
    fetch(`${API_BASE_URL}/youtube/playlists/${playlistId}/videos?maxResults=${maxResults}`)
      .then(res => res.json()),

  searchChannels: (query, maxResults = 20) => 
    fetch(`${API_BASE_URL}/youtube/channels/search?query=${encodeURIComponent(query)}&maxResults=${maxResults}`)
      .then(res => res.json()),

  getChannelVideos: (channelId, maxResults = 20) => 
    fetch(`${API_BASE_URL}/youtube/channels/${channelId}/videos?maxResults=${maxResults}`)
      .then(res => res.json())
};

// Favorites endpoints
export const favoritesAPI = {
  addVideoToFavorites: (token, videoId) => 
    fetch(`${API_BASE_URL}/v1/favorites/add`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ videoId })
    }).then(res => res.json()),

  removeVideoFromFavorites: (token, videoId) => 
    fetch(`${API_BASE_URL}/v1/favorites/remove`, {
      method: 'DELETE',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ videoId })
    }).then(res => res.json()),

  getUserFavorites: (token, page = 0, size = 10) => 
    fetch(`${API_BASE_URL}/v1/favorites/user?page=${page}&size=${size}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  hasUserFavoritedVideo: (token, videoId) => 
    fetch(`${API_BASE_URL}/v1/favorites/has-favorited?videoId=${videoId}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  getFavoriteStatistics: (token) => 
    fetch(`${API_BASE_URL}/v1/favorites/statistics`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  getRecentlyFavoritedVideos: (token, limit = 10) => 
    fetch(`${API_BASE_URL}/v1/favorites/recent?limit=${limit}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  getMostFavoritedVideosOfPeriod: (token, startDate, endDate, limit = 10) => 
    fetch(`${API_BASE_URL}/v1/favorites/most-favorited?startDate=${startDate}&endDate=${endDate}&limit=${limit}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  getTrendingFavoriteVideos: (token, limit = 10) => 
    fetch(`${API_BASE_URL}/v1/favorites/trending?limit=${limit}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  getSimilarFavorites: (token, videoId, limit = 10) => 
    fetch(`${API_BASE_URL}/v1/favorites/similar?videoId=${videoId}&limit=${limit}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  getUsersWithSimilarFavorites: (token, limit = 10) => 
    fetch(`${API_BASE_URL}/v1/favorites/similar-users?limit=${limit}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json()),

  getFavoritesTrendsByTimeOfDay: (token) => 
    fetch(`${API_BASE_URL}/v1/favorites/trends-by-time`, {
      headers: { 'Authorization': `Bearer ${token}` }
    }).then(res => res.json())
};

// Utility functions
export const formatDuration = (duration) => {
  if (!duration) return '0:00';
  // Parse ISO 8601 duration format (PT4M13S)
  const match = duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/);
  if (!match) return '0:00';
  const hours = parseInt(match[1] || 0);
  const minutes = parseInt(match[2] || 0);
  const seconds = parseInt(match[3] || 0);
  if (hours > 0) {
    return `${hours}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  }
  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
};

export const formatViewCount = (count) => {
  if (!count) return '0 views';
  if (count >= 1000000) {
    return `${(count / 1000000).toFixed(1)}M views`;
  } else if (count >= 1000) {
    return `${(count / 1000).toFixed(1)}K views`;
  }
  return `${count} views`;
};

export const formatDate = (dateString) => {
  const date = new Date(dateString);
  const now = new Date();
  const diffInSeconds = Math.floor((now - date) / 1000);
  if (diffInSeconds < 60) return 'Just now';
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)} minutes ago`;
  if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)} hours ago`;
  if (diffInSeconds < 2592000) return `${Math.floor(diffInSeconds / 86400)} days ago`;
  if (diffInSeconds < 31536000) return `${Math.floor(diffInSeconds / 2592000)} months ago`;
  return `${Math.floor(diffInSeconds / 31536000)} years ago`;
};