import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

function Header() {
  const { isAuthenticated, logout } = useAuth(); // 로그인 여부 + logout 함수
  const navigate = useNavigate();

  // 로그아웃 버튼 클릭 시 실행될 함수
  const handleLogout = (e) => {
    e.preventDefault(); // a 태그 기본 동작 방지
    logout();           // useAuth 훅에서 제공되는 logout 로직 (예: 토큰 삭제 등)
    navigate('/');      // 메인 페이지로 이동
  };

  return (
    <header style={{
      display: 'flex', 
      justifyContent: 'space-between', 
      alignItems: 'center',
      background: '#eee',
      padding: '8px'
    }}>
      {/* 좌측 메뉴 */}
      <nav>
        <Link to="/">메인</Link> |{' '}
        <Link to="/prior-art">선행 기술조사 보고서 작성</Link> |{' '}
        <Link to="/patent">특허 명세서 작성</Link> |{' '}
        <Link to="/opinion">특허 의견서 작성</Link> |{' '}
        <Link to="/board">게시판</Link>
      </nav>

      {/* 우측 로그인/마이페이지 */}
      <div>
        {isAuthenticated ? (
          <>
            <Link to="/mypage">마이페이지</Link> |{' '}
            {/* 기존: <Link to="/logout">로그아웃</Link> -> 수정 */}
            <a href="#" onClick={handleLogout}>로그아웃</a>
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
