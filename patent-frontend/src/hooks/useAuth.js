// src/hooks/useAuth.js
import { useState, useEffect } from 'react';

/**
 * 간단한 인증 훅 (토큰 + 닉네임):
 *  - localStorage에 token이 있으면 isAuthenticated=true
 *  - login(token, nickname)으로 로그인
 *  - logout()으로 로그아웃
 */
export function useAuth() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // (추가) user 객체(닉네임 등)
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const nickname = localStorage.getItem('nickname'); // (추가)

    // 토큰이 있으면 로그인 상태
    setIsAuthenticated(!!token);

    // 닉네임도 있으면 user에 저장
    if (nickname) {
      setUser({ nickname });
    }
  }, []);

  /**
   * 로그인: 백엔드에서 받은 토큰 + 닉네임을 localStorage에 저장
   * 예: login(data.token, data.nickname)
   */
  function login(token, nickname) {
    localStorage.setItem('token', token);
    localStorage.setItem('nickname', nickname); // (추가)
    setIsAuthenticated(true);
    setUser({ nickname }); // user 객체에 nickname 저장
  }

  /**
   * 로그아웃: 토큰 + 닉네임 제거
   */
  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('nickname'); // (추가)
    setIsAuthenticated(false);
    setUser(null);
  }

  return {
    isAuthenticated,
    user,          // (추가) user 객체
    login,
    logout,
  };
}
