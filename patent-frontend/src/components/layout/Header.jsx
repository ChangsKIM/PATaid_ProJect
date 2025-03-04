import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/AuthContext';
import { validateTokenAPI } from '../../services/api';
import '../../css/Header.css';

function Header() {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      validateTokenAPI()
        .then((res) => {
          // res.valid 가 false면 로그아웃
          if (!res.valid) {
            logout();
          }
        })
        // 서버 에러 등 발생 시에도 로그아웃
        .catch(() => logout());
    }
  }, [isAuthenticated, logout]);

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
