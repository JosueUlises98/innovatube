import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useAuth } from '../contexts/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock } from 'lucide-react';

const LoginForm = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const { register, handleSubmit, formState: { errors } } = useForm();

  const onSubmit = async (data) => {
    setError(null);
    setIsLoading(true);
    try {
      await login(data);
      navigate('/');
    } catch (err) {
      setError('Credenciales incorrectas o error de servidor.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-youtube-dark p-4">
      <div className="w-full max-w-md bg-youtube-card rounded-lg shadow-2xl p-8 border border-youtube-border">
        <h2 className="text-2xl font-bold text-white mb-6 text-center">Iniciar sesión</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Email</label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                type="email"
                {...register('email', { 
                  required: 'El email es requerido',
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: 'Ingresa un email válido'
                  }
                })}
                className="w-full pl-10 pr-4 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Ingresa tu email"
                disabled={isLoading}
              />
            </div>
            {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Contraseña</label>
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                type="password"
                {...register('password', { 
                  required: 'La contraseña es requerida',
                  minLength: {
                    value: 6,
                    message: 'La contraseña debe tener al menos 6 caracteres'
                  }
                })}
                className="w-full pl-10 pr-4 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Ingresa tu contraseña"
                disabled={isLoading}
              />
            </div>
            {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
          </div>
          {error && (
            <div className="bg-red-900/20 border border-red-500/50 rounded-lg p-3">
              <p className="text-red-400 text-sm text-center">{error}</p>
            </div>
          )}
          <button
            type="submit"
            disabled={isLoading}
            className="w-full py-3 rounded-lg bg-youtube-accent text-white font-semibold hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 focus:ring-offset-youtube-card"
          >
            {isLoading ? (
              <div className="flex items-center justify-center">
                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                Iniciando sesión...
              </div>
            ) : (
              'Iniciar sesión'
            )}
          </button>
        </form>
        <div className="text-center mt-6">
          <span className="text-youtube-textMuted">¿No tienes cuenta?</span>{' '}
          <Link to="/register" className="text-youtube-accent hover:underline font-medium transition-colors">
            Regístrate
          </Link>
        </div>
        <div className="text-center mt-3">
          <Link to="/recover" className="text-youtube-accent hover:underline text-sm transition-colors">
            ¿Olvidaste tu contraseña?
          </Link>
        </div>
      </div>
    </div>
  );
};

export default LoginForm; 