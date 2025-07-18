import React, { useState, useRef, useEffect } from 'react';
import {
  Play,
  Pause,
  SkipBack,
  SkipForward,
  Volume2,
  VolumeX,
  Maximize,
  Minimize,
  Settings,
  Captions,
  RotateCcw,
  RotateCw,
  Download,
  Share2,
  Heart,
  ThumbsUp,
  ThumbsDown,
  MessageCircle,
  MoreVertical,
  Clock,
  Eye,
  User
} from 'lucide-react';
import { formatDuration, formatViewCount, formatDate } from '../lib/api';

const VideoPlayer = ({ 
  video, 
  isYouTubeVideo = false,
  onFavoriteToggle,
  onLike,
  onDislike,
  onShare,
  onComment,
  autoPlay = false,
  showControls = true,
  className = ""
}) => {
  const videoRef = useRef(null);
  const containerRef = useRef(null);
  const [isPlaying, setIsPlaying] = useState(autoPlay);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [volume, setVolume] = useState(1);
  const [isMuted, setIsMuted] = useState(false);
  const [isFullscreen, setIsFullscreen] = useState(false);
  const [showVolumeSlider, setShowVolumeSlider] = useState(false);
  const [showSettings, setShowSettings] = useState(false);
  const [playbackRate, setPlaybackRate] = useState(1);
  const [quality, setQuality] = useState('auto');
  const [showCaptions, setShowCaptions] = useState(false);
  const [isLiked, setIsLiked] = useState(false);
  const [isDisliked, setIsDisliked] = useState(false);
  const [isFavorited, setIsFavorited] = useState(false);
  const [showControls, setShowControls] = useState(showControls);
  const [controlsTimeout, setControlsTimeout] = useState(null);

  // Video metadata
  const [videoStats, setVideoStats] = useState({
    viewCount: video?.viewCount || 0,
    likeCount: video?.likeCount || 0,
    dislikeCount: video?.dislikeCount || 0,
    commentCount: video?.commentCount || 0
  });

  useEffect(() => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    const handleLoadedMetadata = () => {
      setDuration(videoElement.duration);
    };

    const handleTimeUpdate = () => {
      setCurrentTime(videoElement.currentTime);
    };

    const handleVolumeChange = () => {
      setVolume(videoElement.volume);
      setIsMuted(videoElement.muted);
    };

    const handlePlay = () => setIsPlaying(true);
    const handlePause = () => setIsPlaying(false);

    videoElement.addEventListener('loadedmetadata', handleLoadedMetadata);
    videoElement.addEventListener('timeupdate', handleTimeUpdate);
    videoElement.addEventListener('volumechange', handleVolumeChange);
    videoElement.addEventListener('play', handlePlay);
    videoElement.addEventListener('pause', handlePause);

    return () => {
      videoElement.removeEventListener('loadedmetadata', handleLoadedMetadata);
      videoElement.removeEventListener('timeupdate', handleTimeUpdate);
      videoElement.removeEventListener('volumechange', handleVolumeChange);
      videoElement.removeEventListener('play', handlePlay);
      videoElement.removeEventListener('pause', handlePause);
    };
  }, []);

  // Auto-hide controls
  useEffect(() => {
    if (!showControls) return;

    const handleMouseMove = () => {
      setShowControls(true);
      if (controlsTimeout) clearTimeout(controlsTimeout);
      const timeout = setTimeout(() => setShowControls(false), 3000);
      setControlsTimeout(timeout);
    };

    const handleMouseLeave = () => {
      if (controlsTimeout) clearTimeout(controlsTimeout);
      const timeout = setTimeout(() => setShowControls(false), 1000);
      setControlsTimeout(timeout);
    };

    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseleave', handleMouseLeave);

    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseleave', handleMouseLeave);
      if (controlsTimeout) clearTimeout(controlsTimeout);
    };
  }, [showControls, controlsTimeout]);

  const togglePlay = () => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    if (isPlaying) {
      videoElement.pause();
    } else {
      videoElement.play();
    }
  };

  const handleSeek = (e) => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    const rect = e.currentTarget.getBoundingClientRect();
    const clickX = e.clientX - rect.left;
    const width = rect.width;
    const seekTime = (clickX / width) * duration;
    
    videoElement.currentTime = seekTime;
    setCurrentTime(seekTime);
  };

  const handleVolumeChange = (e) => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    const newVolume = parseFloat(e.target.value);
    videoElement.volume = newVolume;
    setVolume(newVolume);
    setIsMuted(newVolume === 0);
  };

  const toggleMute = () => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    videoElement.muted = !isMuted;
    setIsMuted(!isMuted);
  };

  const skipTime = (seconds) => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    const newTime = Math.max(0, Math.min(duration, currentTime + seconds));
    videoElement.currentTime = newTime;
    setCurrentTime(newTime);
  };

  const toggleFullscreen = () => {
    if (!document.fullscreenElement) {
      containerRef.current?.requestFullscreen();
      setIsFullscreen(true);
    } else {
      document.exitFullscreen();
      setIsFullscreen(false);
    }
  };

  const handlePlaybackRateChange = (rate) => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    videoElement.playbackRate = rate;
    setPlaybackRate(rate);
    setShowSettings(false);
  };

  const handleLike = () => {
    setIsLiked(!isLiked);
    if (isDisliked) setIsDisliked(false);
    if (onLike) onLike();
  };

  const handleDislike = () => {
    setIsDisliked(!isDisliked);
    if (isLiked) setIsLiked(false);
    if (onDislike) onDislike();
  };

  const handleFavorite = () => {
    setIsFavorited(!isFavorited);
    if (onFavoriteToggle) onFavoriteToggle();
  };

  const handleShare = () => {
    if (onShare) onShare();
  };

  const handleComment = () => {
    if (onComment) onComment();
  };

  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  const getVideoSource = () => {
    if (isYouTubeVideo && video?.videoId) {
      return `https://www.youtube.com/embed/${video.videoId}?autoplay=${autoPlay ? 1 : 0}&controls=0&modestbranding=1&rel=0`;
    }
    return video?.videoUrl || video?.url || '';
  };

  return (
    <div 
      ref={containerRef}
      className={`relative bg-black rounded-lg overflow-hidden ${className}`}
      onMouseEnter={() => setShowControls(true)}
      onMouseLeave={() => {
        if (controlsTimeout) clearTimeout(controlsTimeout);
        const timeout = setTimeout(() => setShowControls(false), 2000);
        setControlsTimeout(timeout);
      }}
    >
      {/* Video Element */}
      {isYouTubeVideo ? (
        <iframe
          ref={videoRef}
          src={getVideoSource()}
          className="w-full h-full"
          frameBorder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
        />
      ) : (
        <video
          ref={videoRef}
          src={getVideoSource()}
          className="w-full h-full"
          autoPlay={autoPlay}
          muted={isMuted}
          onLoadedMetadata={() => setDuration(videoRef.current?.duration || 0)}
          onTimeUpdate={() => setCurrentTime(videoRef.current?.currentTime || 0)}
        />
      )}

      {/* Overlay Controls */}
      {showControls && (
        <>
          {/* Top Controls */}
          <div className="absolute top-0 left-0 right-0 bg-gradient-to-b from-black/70 to-transparent p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-4">
                <button
                  onClick={toggleFullscreen}
                  className="text-white hover:text-red-500 transition-colors"
                >
                  {isFullscreen ? <Minimize size={20} /> : <Maximize size={20} />}
                </button>
                <button
                  onClick={() => setShowSettings(!showSettings)}
                  className="text-white hover:text-red-500 transition-colors"
                >
                  <Settings size={20} />
                </button>
              </div>
              
              <div className="flex items-center space-x-4">
                <button
                  onClick={handleFavorite}
                  className={`transition-colors ${isFavorited ? 'text-red-500' : 'text-white hover:text-red-500'}`}
                >
                  <Heart size={20} className={isFavorited ? 'fill-current' : ''} />
                </button>
                <button
                  onClick={handleShare}
                  className="text-white hover:text-red-500 transition-colors"
                >
                  <Share2 size={20} />
                </button>
              </div>
            </div>
          </div>

          {/* Center Play Button */}
          <div className="absolute inset-0 flex items-center justify-center">
            <button
              onClick={togglePlay}
              className="bg-black/50 hover:bg-black/70 rounded-full p-4 transition-all duration-200"
            >
              {isPlaying ? <Pause size={32} /> : <Play size={32} />}
            </button>
          </div>

          {/* Bottom Controls */}
          <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-4">
            {/* Progress Bar */}
            <div className="mb-4">
              <div
                className="w-full h-1 bg-gray-600 rounded-full cursor-pointer"
                onClick={handleSeek}
              >
                <div
                  className="h-full bg-red-600 rounded-full transition-all duration-200"
                  style={{ width: `${(currentTime / duration) * 100}%` }}
                />
              </div>
              <div className="flex justify-between text-white text-sm mt-1">
                <span>{formatTime(currentTime)}</span>
                <span>{formatTime(duration)}</span>
              </div>
            </div>

            {/* Control Buttons */}
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-4">
                <button
                  onClick={() => skipTime(-10)}
                  className="text-white hover:text-red-500 transition-colors"
                >
                  <SkipBack size={20} />
                </button>
                
                <button
                  onClick={togglePlay}
                  className="text-white hover:text-red-500 transition-colors"
                >
                  {isPlaying ? <Pause size={24} /> : <Play size={24} />}
                </button>
                
                <button
                  onClick={() => skipTime(10)}
                  className="text-white hover:text-red-500 transition-colors"
                >
                  <SkipForward size={20} />
                </button>

                {/* Volume Control */}
                <div className="relative flex items-center space-x-2">
                  <button
                    onClick={toggleMute}
                    className="text-white hover:text-red-500 transition-colors"
                    onMouseEnter={() => setShowVolumeSlider(true)}
                    onMouseLeave={() => setShowVolumeSlider(false)}
                  >
                    {isMuted || volume === 0 ? <VolumeX size={20} /> : <Volume2 size={20} />}
                  </button>
                  
                  {showVolumeSlider && (
                    <div className="absolute bottom-full left-0 mb-2 bg-black/90 rounded p-2">
                      <input
                        type="range"
                        min="0"
                        max="1"
                        step="0.1"
                        value={volume}
                        onChange={handleVolumeChange}
                        className="w-20 h-1 bg-gray-600 rounded-full appearance-none cursor-pointer"
                        style={{
                          background: `linear-gradient(to right, #dc2626 0%, #dc2626 ${volume * 100}%, #4b5563 ${volume * 100}%, #4b5563 100%)`
                        }}
                      />
                    </div>
                  )}
                </div>
              </div>

              <div className="flex items-center space-x-4">
                <button
                  onClick={handleLike}
                  className={`transition-colors ${isLiked ? 'text-red-500' : 'text-white hover:text-red-500'}`}
                >
                  <ThumbsUp size={20} className={isLiked ? 'fill-current' : ''} />
                </button>
                
                <button
                  onClick={handleDislike}
                  className={`transition-colors ${isDisliked ? 'text-red-500' : 'text-white hover:text-red-500'}`}
                >
                  <ThumbsDown size={20} className={isDisliked ? 'fill-current' : ''} />
                </button>
                
                <button
                  onClick={handleComment}
                  className="text-white hover:text-red-500 transition-colors"
                >
                  <MessageCircle size={20} />
                </button>
              </div>
            </div>
          </div>

          {/* Settings Menu */}
          {showSettings && (
            <div className="absolute top-16 right-4 bg-black/90 rounded-lg p-4 min-w-48">
              <div className="space-y-2">
                <div>
                  <h4 className="text-white text-sm font-medium mb-2">Playback Speed</h4>
                  <div className="space-y-1">
                    {[0.25, 0.5, 0.75, 1, 1.25, 1.5, 2].map((rate) => (
                      <button
                        key={rate}
                        onClick={() => handlePlaybackRateChange(rate)}
                        className={`block w-full text-left px-2 py-1 rounded text-sm transition-colors ${
                          playbackRate === rate
                            ? 'bg-red-600 text-white'
                            : 'text-gray-300 hover:text-white hover:bg-gray-700'
                        }`}
                      >
                        {rate}x
                      </button>
                    ))}
                  </div>
                </div>
                
                <div>
                  <h4 className="text-white text-sm font-medium mb-2">Quality</h4>
                  <div className="space-y-1">
                    {['auto', '1080p', '720p', '480p', '360p'].map((q) => (
                      <button
                        key={q}
                        onClick={() => setQuality(q)}
                        className={`block w-full text-left px-2 py-1 rounded text-sm transition-colors ${
                          quality === q
                            ? 'bg-red-600 text-white'
                            : 'text-gray-300 hover:text-white hover:bg-gray-700'
                        }`}
                      >
                        {q}
                      </button>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          )}
        </>
      )}

      {/* Video Info Overlay */}
      {video && (
        <div className="absolute top-4 left-4 right-4 pointer-events-none">
          <div className="bg-black/50 rounded-lg p-4 backdrop-blur-sm">
            <h2 className="text-white text-lg font-semibold mb-2">{video.title}</h2>
            
            <div className="flex items-center justify-between text-sm text-gray-300">
              <div className="flex items-center space-x-4">
                <div className="flex items-center space-x-1">
                  <Eye size={14} />
                  <span>{formatViewCount(videoStats.viewCount)}</span>
                </div>
                
                <div className="flex items-center space-x-1">
                  <ThumbsUp size={14} />
                  <span>{formatViewCount(videoStats.likeCount)}</span>
                </div>
                
                <div className="flex items-center space-x-1">
                  <MessageCircle size={14} />
                  <span>{formatViewCount(videoStats.commentCount)}</span>
                </div>
              </div>
              
              <div className="flex items-center space-x-1">
                <Clock size={14} />
                <span>{formatDate(video.publishedAt)}</span>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default VideoPlayer; 