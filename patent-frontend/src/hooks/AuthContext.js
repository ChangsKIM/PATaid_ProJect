// src/hooks/AuthContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  // 로컬 스토리지에서 토큰/닉네임/권한 정보를 읽어오는 '로딩 완료' 여부
  const [isAuthLoaded, setIsAuthLoaded] = useState(false);

  useEffect(() => {
    // 앱이 처음 로드될 때 localStorage에서 로그인 정보를 읽어옴
    const token = localStorage.getItem('token');
    const nickname = localStorage.getItem('nickname');
    const role = localStorage.getItem('role');

    if (token) {
      setIsAuthenticated(true);
      setUser({ nickname, role });
    }
    // 로컬 스토리지 읽기가 끝났음을 표시
    setIsAuthLoaded(true);
  }, []);

  function login(token, nickname, role) {
    localStorage.setItem('token', token);
    localStorage.setItem('nickname', nickname);
    localStorage.setItem('role', role);

    setIsAuthenticated(true);
    setUser({ nickname, role });
  }

  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('nickname');
    localStorage.removeItem('role');

    setIsAuthenticated(false);
    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        user,
        login,
        logout,
        isAuthLoaded,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
