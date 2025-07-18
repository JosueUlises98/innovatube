import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useAuth } from '../contexts/AuthContext';
import { userAPI } from '../lib/api';
import { Mail, User, Lock } from 'lucide-react';

const ProfilePage = () => {
  const { user, token } = useAuth();
  const [serverError, setServerError] = useState(null);
  const { register, handleSubmit, setValue, formState: { errors, isDirty, isSubmitting } } = useForm();

  useEffect(() => {
    if (user) {
      setValue('name', user.name);
      setValue('lastname', user.lastname);
      setValue('username', user.username);
      setValue('email', user.email);
    }
  }, [user, setValue]);

  const onSubmit = async (data) => {
    setServerError(null);
    try {
      await userAPI.updateProfile(token, data);
      window.location.reload();
    } catch (err) {
      setServerError('Error al actualizar el perfil.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-youtube-dark">
      <div className="w-full max-w-lg bg-youtube-card rounded-lg shadow-lg p-8">
        <h2 className="text-2xl font-bold text-white mb-6 text-center">Mi Perfil</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          <div>
            <label className="block text-youtube-textMuted mb-1">Nombre</label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                type="text"
                {...register('name', { required: 'El nombre es requerido' })}
                className="w-full pl-10 pr-4 py-2 rounded bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent"
                placeholder="Nombre"
              />
            </div>
            {errors.name && <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-1">Apellido</label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                type="text"
                {...register('lastname', { required: 'El apellido es requerido' })}
                className="w-full pl-10 pr-4 py-2 rounded bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent"
                placeholder="Apellido"
              />
            </div>
            {errors.lastname && <p className="text-red-500 text-sm mt-1">{errors.lastname.message}</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-1">Nombre de Usuario</label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                type="text"
                {...register('username', { required: 'El nombre de usuario es requerido' })}
                className="w-full pl-10 pr-4 py-2 rounded bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent"
                placeholder="Nombre de usuario"
              />
            </div>
            {errors.username && <p className="text-red-500 text-sm mt-1">{errors.username.message}</p>}
          </div>
          <div>
            <label className="block text-youtube-textMuted mb-1">Email</label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                type="email"
                {...register('email', { required: 'El email es requerido' })}
                className="w-full pl-10 pr-4 py-2 rounded bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent"
                placeholder="Email"
              />
            </div>
            {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
          </div>
          {serverError && <p className="text-red-500 text-center text-sm">{serverError}</p>}
          <button
            type="submit"
            disabled={!isDirty || isSubmitting}
            className="w-full py-2 rounded bg-youtube-accent text-white font-semibold hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Guardar cambios
          </button>
        </form>
      </div>
    </div>
  );
};

export default ProfilePage; 