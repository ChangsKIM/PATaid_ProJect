// src/hooks/useAuth.js

import { useState, useEffect } from 'react';

/**
 * 간단한 인증 훅:
 *  - localStorage에 token이 있으면 isAuthenticated=true
 *  - login(token)으로 로그인
 *  - logout()으로 로그아웃
 */
export function useAuth() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    setIsAuthenticated(!!token); // token이 있으면 true, 없으면 false
  }, []);

  /**
   * 로그인: 백엔드에서 받은 토큰을 localStorage에 저장
   */
  function login(token) {
    localStorage.setItem('token', token);
    setIsAuthenticated(true);
  }

  /**
   * 로그아웃: 토큰 제거
   */
  function logout() {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
  }

  return {
    isAuthenticated,
    login,
    logout,
  };
}
