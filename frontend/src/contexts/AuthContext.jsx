import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI, userAPI } from '../lib/api';
import toast from 'react-hot-toast';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser));
      } catch (error) {
        console.error('Error parsing saved user:', error);
        localStorage.removeItem('currentUser');
      }
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    try {
      setLoading(true);
      const userData = await authAPI.login(credentials);
      setUser(userData);
      localStorage.setItem('currentUser', JSON.stringify(userData));
      toast.success('¡Inicio de sesión exitoso!');
      return userData;
    } catch (error) {
      console.error('Login error:', error);
      const message = error.response?.status === 401 
        ? 'Credenciales inválidas' 
        : 'Error al iniciar sesión';
      toast.error(message);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const register = async (userData) => {
    try {
      setLoading(true);
      
      // Check username availability
      const isUsernameAvailable = await userAPI.checkUsernameAvailability(userData.username);
      if (!isUsernameAvailable) {
        toast.error('El nombre de usuario ya está en uso');
        throw new Error('Username not available');
      }

      // Check email availability
      const isEmailAvailable = await userAPI.checkEmailAvailability(userData.email);
      if (!isEmailAvailable) {
        toast.error('El email ya está registrado');
        throw new Error('Email not available');
      }

      // Register user
      const newUser = await authAPI.register(userData);
      setUser(newUser);
      localStorage.setItem('currentUser', JSON.stringify(newUser));
      toast.success('¡Registro exitoso! Ahora puedes iniciar sesión');
      return newUser;
    } catch (error) {
      console.error('Registration error:', error);
      if (error.message !== 'Username not available' && error.message !== 'Email not available') {
        toast.error('Error al registrar usuario');
      }
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      await authAPI.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setUser(null);
      localStorage.removeItem('currentUser');
      toast.success('Sesión cerrada');
    }
  };

  const updateUser = (userData) => {
    setUser(userData);
    localStorage.setItem('currentUser', JSON.stringify(userData));
  };

  const value = {
    user,
    loading,
    login,
    register,
    logout,
    updateUser,
    isAuthenticated: !!user,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}; 