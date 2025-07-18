import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Mail } from 'lucide-react';

const RecoverPasswordPage = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const onSubmit = async (data) => {
    setMessage(null);
    setError(null);
    try {
      // Aquí iría la lógica real de recuperación
      setMessage('Si el email existe, recibirás instrucciones para recuperar tu contraseña.');
    } catch (err) {
      setError('Error al intentar recuperar la contraseña.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-youtube-dark">
      <div className="w-full max-w-md bg-youtube-card rounded-lg shadow-lg p-8">
        <h2 className="text-2xl font-bold text-white mb-6 text-center">Recuperar contraseña</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          <div>
            <label className="block text-youtube-textMuted mb-1">Email</label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-youtube-textMuted" size={20} />
              <input
                type="email"
                {...register('email', { required: 'El email es requerido' })}
                className="w-full pl-10 pr-4 py-2 rounded bg-youtube-dark text-white border border-youtube-border focus:outline-none focus:ring-2 focus:ring-youtube-accent"
                placeholder="Ingresa tu email"
              />
            </div>
            {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
          </div>
          {error && <p className="text-red-500 text-center text-sm">{error}</p>}
          {message && <p className="text-green-500 text-center text-sm">{message}</p>}
          <button
            type="submit"
            className="w-full py-2 rounded bg-youtube-accent text-white font-semibold hover:bg-red-700 transition-colors"
          >
            Enviar instrucciones
          </button>
        </form>
      </div>
    </div>
  );
};

export default RecoverPasswordPage; 