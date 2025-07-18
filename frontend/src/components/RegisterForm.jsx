import React, { useState, useRef } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import { Eye, EyeOff, Mail, Lock, User, UserCheck } from 'lucide-react';
import ReCAPTCHA from 'react-google-recaptcha';
import { userAPI } from '../lib/api';

const RegisterForm = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [recaptchaValue, setRecaptchaValue] = useState(null);
  const [usernameExists, setUsernameExists] = useState(null);
  const [emailExists, setEmailExists] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const recaptchaRef = useRef();

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const password = watch('password');

  const onSubmit = async (data) => {
    setError(null);
    setIsLoading(true);
    try {
      if (!recaptchaRef.current) {
        setError('Error: ReCaptcha no está disponible. Por favor recarga la página.');
        return;
      }
      recaptchaRef.current.reset();
      await new Promise(resolve => setTimeout(resolve, 1000));
      const recaptchaToken = await recaptchaRef.current.executeAsync();
      if (!recaptchaToken) {
        setError('Por favor completa el ReCaptcha');
        return;
      }
      const userData = {
        ...data,
        recaptchaToken: recaptchaToken
      };
      const response = await userAPI.register(userData);
      if (response && response.success === false) {
        setError('Error en el registro: ' + (response.message || 'Error desconocido'));
        return;
      }
      navigate('/login');
    } catch (error) {
      setError('Error en el registro. Por favor intenta de nuevo.');
    } finally {
      setIsLoading(false);
    }
  };

  const checkUsername = async (username) => {
    if (!username) {
      setUsernameExists(null);
      return;
    }
    try {
      const isAvailable = await userAPI.checkUsernameAvailability(username);
      setUsernameExists(isAvailable);
    } catch {
      setUsernameExists(null);
    }
  };

  const checkEmail = async (email) => {
    if (!email) {
      setEmailExists(null);
      return;
    }
    try {
      const isAvailable = await userAPI.checkEmailAvailability(email);
      setEmailExists(isAvailable);
    } catch {
      setEmailExists(null);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-youtube-dark p-4">
      <div className="w-full max-w-md bg-youtube-card rounded-lg shadow-2xl p-8 border border-youtube-border max-h-[90vh] overflow-y-auto">
        <h2 className="text-2xl font-bold text-white mb-6 text-center">Registrarse</h2>
        <form className="space-y-5" onSubmit={handleSubmit(onSubmit)}>
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Nombre</label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                id="name"
                type="text"
                {...register('name', { 
                  required: 'El nombre es requerido', 
                  minLength: { value: 2, message: 'El nombre debe tener al menos 2 caracteres' } 
                })}
                className="w-full pl-10 pr-4 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Ingresa tu nombre"
                disabled={isLoading}
              />
            </div>
            {errors.name && <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Apellido</label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                id="lastname"
                type="text"
                {...register('lastname', { 
                  required: 'El apellido es requerido', 
                  minLength: { value: 2, message: 'El apellido debe tener al menos 2 caracteres' } 
                })}
                className="w-full pl-10 pr-4 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Ingresa tu apellido"
                disabled={isLoading}
              />
            </div>
            {errors.lastname && <p className="text-red-500 text-sm mt-1">{errors.lastname.message}</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Nombre de Usuario</label>
            <div className="relative">
              <UserCheck className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                id="username"
                type="text"
                {...register('username', { 
                  required: 'El nombre de usuario es requerido', 
                  minLength: { value: 3, message: 'El nombre de usuario debe tener al menos 3 caracteres' }, 
                  pattern: { value: /^[a-zA-Z0-9_]+$/, message: 'Solo se permiten letras, números y guiones bajos' }, 
                  onBlur: (e) => checkUsername(e.target.value) 
                })}
                className="w-full pl-10 pr-4 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Ingresa tu nombre de usuario"
                disabled={isLoading}
              />
            </div>
            {errors.username && <p className="text-red-500 text-sm mt-1">{errors.username.message}</p>}
            {usernameExists === false && <p className="text-red-500 text-sm mt-1">Nombre de usuario no disponible</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Email</label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                id="email"
                type="email"
                {...register('email', { 
                  required: 'El email es requerido', 
                  pattern: { value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i, message: 'Ingresa un email válido' }, 
                  onBlur: (e) => checkEmail(e.target.value) 
                })}
                className="w-full pl-10 pr-4 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Ingresa tu email"
                disabled={isLoading}
              />
            </div>
            {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
            {emailExists === false && <p className="text-red-500 text-sm mt-1">Email no disponible</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Contraseña</label>
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                id="password"
                type={showPassword ? 'text' : 'password'}
                {...register('password', { 
                  required: 'La contraseña es requerida', 
                  minLength: { value: 6, message: 'La contraseña debe tener al menos 6 caracteres' } 
                })}
                className="w-full pl-10 pr-10 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Ingresa tu contraseña"
                disabled={isLoading}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-youtube-textMuted hover:text-white transition-colors"
                disabled={isLoading}
              >
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
            {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-2 font-medium">Confirmar Contraseña</label>
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                id="confirmPassword"
                type={showConfirmPassword ? 'text' : 'password'}
                {...register('confirmPassword', { 
                  required: 'Confirma tu contraseña', 
                  validate: value => value === password || 'Las contraseñas no coinciden' 
                })}
                className="w-full pl-10 pr-10 py-3 rounded-lg bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent focus:border-transparent transition-all"
                placeholder="Confirma tu contraseña"
                disabled={isLoading}
              />
              <button
                type="button"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-youtube-textMuted hover:text-white transition-colors"
                disabled={isLoading}
              >
                {showConfirmPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
            {errors.confirmPassword && <p className="text-red-500 text-sm mt-1">{errors.confirmPassword.message}</p>}
          </div>
          <div className="flex justify-center">
            <ReCAPTCHA
              ref={recaptchaRef}
              sitekey="6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI"
              size="invisible"
              onExpired={() => setRecaptchaValue(null)}
              onError={() => setRecaptchaValue(null)}
            />
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
                Registrando...
              </div>
            ) : (
              'Registrarse'
            )}
          </button>
        </form>
        <div className="text-center mt-6">
          <span className="text-youtube-textMuted">¿Ya tienes cuenta?</span>{' '}
          <Link to="/login" className="text-youtube-accent hover:underline font-medium transition-colors">
            Inicia sesión aquí
          </Link>
        </div>
      </div>
    </div>
  );
};

export default RegisterForm; 