// src/pages/MyPage.jsx
import React from 'react';
import { useAuth } from '../hooks/useAuth';
import { useNavigate } from 'react-router-dom';

function MyPage() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout(); // localStorage에서 token 제거, isAuthenticated=false
    navigate('/'); // 홈 등 원하는 페이지로 이동
  };

  return (
    <div>
      <h2>마이페이지</h2>
      <p>여기에 내 정보 표시 등...</p>
      <button onClick={handleLogout}>로그아웃</button>
    </div>
  );
}

export default MyPage;
