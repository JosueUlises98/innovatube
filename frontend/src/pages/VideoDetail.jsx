import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { videoAPI } from '../lib/api';
import VideoCard from '../components/VideoCard';

const VideoDetail = () => {
  const { id } = useParams();
  const [video, setVideo] = useState(null);
  const [related, setRelated] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      const data = await videoAPI.getVideoByYoutubeId(id);
      setVideo(data);
      const relatedVideos = await videoAPI.getYouTubeRelatedVideos(id);
      setRelated(relatedVideos || []);
      setLoading(false);
    };
    fetchData();
  }, [id]);

  if (loading) {
    return <div className="min-h-screen bg-youtube-dark text-youtube-textMuted flex items-center justify-center">Cargando...</div>;
  }

  if (!video) {
    return <div className="min-h-screen bg-youtube-dark text-youtube-textMuted flex items-center justify-center">Video no encontrado.</div>;
  }

  return (
    <div className="min-h-screen bg-youtube-dark py-8 px-4 flex flex-col lg:flex-row gap-8">
      <div className="flex-1 max-w-3xl mx-auto">
        <div className="aspect-video bg-black rounded-lg overflow-hidden mb-4">
          <iframe
            src={`https://www.youtube.com/embed/${video.youtubeId}`}
            title={video.title}
            allowFullScreen
            className="w-full h-full"
          />
        </div>
        <h1 className="text-2xl font-bold text-white mb-2">{video.title}</h1>
        <div className="text-youtube-textMuted text-sm mb-4">
          {video.channelTitle} • {video.viewCount} vistas • {video.publishedAt}
        </div>
        <div className="bg-youtube-card rounded-lg p-4 text-white mb-4">
          {video.description}
        </div>
      </div>
      <aside className="w-full lg:w-96">
        <h3 className="text-lg font-semibold text-white mb-4">Videos relacionados</h3>
        <div className="flex flex-col gap-4">
          {related.map((v) => (
            <VideoCard key={v.id} video={v} />
          ))}
        </div>
      </aside>
    </div>
  );
};

export default VideoDetail; 