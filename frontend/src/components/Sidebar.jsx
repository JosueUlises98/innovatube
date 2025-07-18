import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Home, Flame, Video, BookOpen, Clock, ThumbsUp, User } from 'lucide-react';

const menu = [
  { label: 'Inicio', icon: <Home size={22} />, to: '/' },
  { label: 'Tendencias', icon: <Flame size={22} />, to: '/trending' },
  { label: 'Suscripciones', icon: <Video size={22} />, to: '/subscriptions' },
  { label: 'Biblioteca', icon: <BookOpen size={22} />, to: '/library' },
  { label: 'Historial', icon: <Clock size={22} />, to: '/history' },
  { label: 'Videos que me gustan', icon: <ThumbsUp size={22} />, to: '/favorites' },
  { label: 'Mi perfil', icon: <User size={22} />, to: '/profile' },
];

const Sidebar = () => {
  const location = useLocation();
  return (
    <aside className="hidden md:flex flex-col fixed top-16 left-0 w-60 h-[calc(100vh-4rem)] bg-youtube-darker border-r border-youtube-border py-4 z-40">
      <nav className="flex flex-col gap-1">
        {menu.map((item) => (
          <Link
            key={item.to}
            to={item.to}
            className={`flex items-center gap-4 px-6 py-2 rounded-lg text-base font-medium transition-colors
              ${location.pathname === item.to ? 'bg-youtube-card text-white' : 'text-youtube-text hover:bg-youtube-card hover:text-white'}`}
          >
            {item.icon}
            {item.label}
          </Link>
        ))}
      </nav>
    </aside>
  );
};

export default Sidebar; 