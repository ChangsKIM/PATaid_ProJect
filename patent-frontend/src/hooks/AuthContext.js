import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const nickname = localStorage.getItem('nickname');
    console.log('useAuth init token:', token, 'nickname:', nickname); // 디버깅
    setIsAuthenticated(!!token);
    if (nickname) {
      setUser({ nickname });
    }
  }, []);

  function login(token, nickname) {
    console.log('useAuth.login called with:', token, nickname); // 디버깅
    localStorage.setItem('token', token);
    localStorage.setItem('nickname', nickname);
    setIsAuthenticated(true);
    setUser({ nickname });
  }

  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('nickname');
    setIsAuthenticated(false);
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
