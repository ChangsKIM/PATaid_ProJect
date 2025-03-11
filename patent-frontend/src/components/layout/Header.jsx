// src/components/layout/Header.jsx
import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/AuthContext';
import { validateTokenAPI } from '../../services/api';
import '../../css/Header.css';

function Header() {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    // 로그인된 상태라면, 토큰 유효성 검증
    if (isAuthenticated) {
      validateTokenAPI()
        .then((res) => {
          // res.valid가 false면, 토큰이 만료되었거나 유효하지 않음 → 로그아웃
          if (!res.valid) {
            logout();
          }
        })
        .catch(() => {
          // 서버 에러 등 발생 시에도 안전하게 로그아웃 처리
          logout();
        });
    }
  }, [isAuthenticated, logout]);

  // 로그아웃 후 메인("/")으로 이동
  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="site-header">
      <nav className="header-nav">
        <Link to="/">메인</Link> |{' '}
        <Link to="/prior-art">선행 기술조사</Link> |{' '}
        <Link to="/patent">특허 명세서 작성</Link> |{' '}
        <Link to="/opinion">특허 의견서 작성</Link> |{' '}
        <Link to="/board">게시판</Link>
      </nav>

      <div className="header-user">
        {isAuthenticated ? (
          <>
            {/* user가 존재하면 닉네임, 없으면 "사용자"로 표시 */}
            <span>{user?.nickname || '사용자'} 님</span> |{' '}
            <Link to="/mypage">마이페이지</Link> |{' '}
            <button onClick={handleLogout}>로그아웃</button>
          </>
        ) : (
          <>
            <Link to="/login">로그인</Link> |{' '}
            <Link to="/register">회원가입</Link>
          </>
        )}
      </div>
    </header>
  );
}

export default Header;
