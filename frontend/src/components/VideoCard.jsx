import React from 'react';
import { Link } from 'react-router-dom';

const VideoCard = ({ video }) => {
  // Handle different video ID fields
  const videoId = video.id || video.youtubeVideoId || video.videoId;
  
  // Handle different data structures for local vs YouTube videos
  const title = video.title || video.snippet?.title || 'Sin título';
  const thumbnailUrl = video.thumbnailUrl || video.snippet?.thumbnails?.default?.url || video.snippet?.thumbnails?.medium?.url || '';
  const channelTitle = video.channelTitle || video.snippet?.channelTitle || 'Canal desconocido';
  const viewCount = video.viewCount || video.statistics?.viewCount || 0;
  const publishedAt = video.publishedAt || video.snippet?.publishedAt || video.addedAt || 'Fecha desconocida';
  
  // Format view count
  const formatViewCount = (count) => {
    if (count >= 1000000) {
      return `${(count / 1000000).toFixed(1)}M`;
    } else if (count >= 1000) {
      return `${(count / 1000).toFixed(1)}K`;
    }
    return count.toString();
  };

  // Format date
  const formatDate = (dateString) => {
    try {
      const date = new Date(dateString);
      const now = new Date();
      const diffTime = Math.abs(now - date);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      
      if (diffDays === 1) return 'Hoy';
      if (diffDays <= 7) return `Hace ${diffDays} días`;
      if (diffDays <= 30) return `Hace ${Math.floor(diffDays / 7)} semanas`;
      if (diffDays <= 365) return `Hace ${Math.floor(diffDays / 30)} meses`;
      return `Hace ${Math.floor(diffDays / 365)} años`;
    } catch (error) {
      return 'Fecha desconocida';
    }
  };

  return (
    <Link to={`/video/${videoId}`} className="block bg-youtube-card rounded-lg overflow-hidden shadow hover:shadow-lg border border-transparent hover:border-youtube-accent transition-all duration-200">
      <div className="relative w-full aspect-video bg-youtube-dark">
        <img 
          src={thumbnailUrl} 
          alt={title} 
          className="w-full h-full object-cover"
          onError={(e) => {
            e.target.src = 'https://via.placeholder.com/320x180/282828/ffffff?text=Video';
          }}
        />
      </div>
      <div className="p-4 flex flex-col gap-1">
        <h3 className="text-white text-lg font-semibold truncate" title={title}>{title}</h3>
        <div className="text-youtube-textMuted text-sm truncate">{channelTitle}</div>
        <div className="text-youtube-textMuted text-xs flex gap-2">
          <span>{formatViewCount(viewCount)} vistas</span>
          <span>•</span>
          <span>{formatDate(publishedAt)}</span>
        </div>
      </div>
    </Link>
  );
};

export default VideoCard; 