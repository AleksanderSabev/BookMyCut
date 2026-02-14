import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authApi, usersApi } from '../api/endpoints';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadUser = useCallback(async () => {
    const token = localStorage.getItem('jwt');
    if (!token) {
      setUser(null);
      setLoading(false);
      return;
    }
    try {
      const me = await usersApi.getMe();
      setUser(me);
    } catch {
      localStorage.removeItem('jwt');
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadUser();
  }, [loadUser]);

  const login = async (username, password) => {
    const res = await authApi.login({ username, password });
    localStorage.setItem('jwt', res.jwtToken);
    setUser({ username: res.username, role: res.role });
  };

  const register = async (username, email, password) => {
    const res = await authApi.register({ username, email, password });
    localStorage.setItem('jwt', res.jwtToken);
    setUser({ username: res.username, role: res.role });
  };

  const logout = async () => {
    try {
      await authApi.logout();
    } finally {
      localStorage.removeItem('jwt');
      setUser(null);
    }
  };

  const isAdmin = user?.role === 'ADMIN';

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout, isAdmin, loadUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
