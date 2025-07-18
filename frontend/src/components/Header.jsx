import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Search, Bell, User } from 'lucide-react';
import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';

const Header = () => {
  const [showAuthModal, setShowAuthModal] = useState(false);
  const [authTab, setAuthTab] = useState('login'); // 'login' o 'register'

  return (
    <header className="fixed top-0 left-0 w-full h-16 bg-youtube-darker flex items-center px-4 z-50 shadow-md">
      {/* Logo */}
      <Link to="/" className="flex items-center mr-6">
        <img src="/favicon-youtube.svg" alt="InnovaTube" className="h-7 w-7 mr-2" />
        <span className="text-xl font-bold tracking-tight text-white">InnovaTube</span>
      </Link>
      {/* Search bar */}
      <form className="flex-1 flex justify-center">
        <div className="relative w-full max-w-xl">
          <input
            type="text"
            placeholder="Buscar"
            className="w-full h-10 pl-4 pr-10 rounded-full bg-youtube-card text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent"
          />
          <button type="submit" className="absolute right-2 top-1/2 -translate-y-1/2 text-youtube-text hover:text-youtube-accent">
            <Search size={20} />
          </button>
        </div>
      </form>
      {/* Icons */}
      <div className="flex items-center gap-4 ml-6">
        <button className="text-youtube-text hover:text-youtube-accent">
          <Bell size={22} />
        </button>
        <button className="text-youtube-text hover:text-youtube-accent" onClick={() => setShowAuthModal(true)}>
          <User size={22} />
        </button>
      </div>
      {/* Modal de Login/Register */}
      {showAuthModal && (
        <>
          <div className="fixed inset-0 bg-black bg-opacity-50 z-40" onClick={() => setShowAuthModal(false)} />
          <div className="absolute left-1/2 top-24 transform -translate-x-1/2 bg-youtube-card rounded-lg p-8 w-full max-w-md z-50 shadow-2xl">
            <button className="absolute top-2 right-2 text-youtube-text hover:text-youtube-accent" onClick={() => setShowAuthModal(false)}>&times;</button>
            <div className="flex justify-center mb-4">
              <button
                className={`px-4 py-2 font-semibold rounded-l ${authTab === 'login' ? 'bg-youtube-accent text-white' : 'bg-youtube-dark text-youtube-text'}`}
                onClick={() => setAuthTab('login')}
              >
                Iniciar sesión
              </button>
              <button
                className={`px-4 py-2 font-semibold rounded-r ${authTab === 'register' ? 'bg-youtube-accent text-white' : 'bg-youtube-dark text-youtube-text'}`}
                onClick={() => setAuthTab('register')}
              >
                Registrarse
              </button>
            </div>
            {authTab === 'login' ? <LoginForm /> : <RegisterForm />}
          </div>
        </>
      )}
    </header>
  );
};

export default Header; 